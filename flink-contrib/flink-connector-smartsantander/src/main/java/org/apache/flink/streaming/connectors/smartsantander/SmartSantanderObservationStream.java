package org.apache.flink.streaming.connectors.smartsantander;

import com.google.gson.*;
import org.apache.flink.streaming.connectors.smartsantander.model.SmartSantanderObservation;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.*;

/**
 * Handles background connections to HTTP API and serializes JSON response into POJOs
 * defined in the model
 *
 * @author federicofdez
 */
public class SmartSantanderObservationStream<T extends SmartSantanderObservation> implements AutoCloseable {

	/**
	 * HTTP client is reused for all API calls
	 */
	private final CloseableHttpClient client;
	/**
	 * Request object is reused for all API calls
	 */
	private final HttpGet request;
	/**
	 * Type of the observations array, necessary to perform deserialization
	 */
	private final Class<T[]> observationsArrayClass;
	/**
	 * Bounded queue of observations
	 */
	private final BlockingQueue<T> observations = new ArrayBlockingQueue<>(500);
	/**
	 * Frequency of updates expressed in seconds
	 */
	private final int updateFrequency;
	/**
	 * Task executor for making requests in separate thread
	 */
	private final ScheduledExecutorService executor;
	/**
	 * Key-value pairs storing the date of the last retrieved data for each sensor
	 */
	private final HashMap<Integer, String> lastModified;


	/**
	 * Creates a source that periodically reads from the API and stores data in a FIFO queue
	 *
	 * @param observationsArrayClass class associated to an array of observation objects
	 *                               (e.g.TrafficObservation[].class)
	 * @param endpoint               URL to make API requests
	 * @param updateFrequency        frequency at which a new request should be made, in seconds
	 */
	SmartSantanderObservationStream(Class<T[]> observationsArrayClass, SmartSantanderAPIEndpoints endpoint, int updateFrequency) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = null;
		try {
			URIBuilder builder = new URIBuilder(endpoint.getUrl());
			builder.setParameter("sort", "asc");
			request = new HttpGet(builder.build());
		} catch (URISyntaxException e) {
			request = new HttpGet(endpoint.getUrl());
		}
		request.setHeader(HttpHeaders.ACCEPT, "application/json");

		this.observationsArrayClass = observationsArrayClass;
		this.client = client;
		this.request = request;
		this.updateFrequency = updateFrequency;
		this.lastModified = new HashMap<Integer, String>();
		this.executor = Executors.newScheduledThreadPool(1);
	}

	public CloseableHttpClient getClient() {
		return client;
	}

	public HttpGet getRequest() {
		return request;
	}

	public int getUpdateFrequency() {
		return updateFrequency;
	}

	public Class<T[]> getObservationsArrayClass() {
		return observationsArrayClass;
	}

	public BlockingQueue<T> getObservations() {
		return observations;
	}

	public ScheduledExecutorService getExecutor() {
		return executor;
	}

	public HashMap<Integer, String> getLastModified() {
		return lastModified;
	}

	/**
	 * Makes HTTP request to the specified endpoint
	 *
	 * @return the response parsed as plain text
	 */
	public String retrieveRawData() {
		String responseBody = "";
		try {
			HttpResponse response = this.getClient().execute(this.getRequest());
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				BufferedReader br;
				br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = br.readLine()) != null) {
					responseBody += line + "\n";
				}
				responseBody = responseBody.substring(0,
					responseBody.lastIndexOf("\n")); // remove last blank line

			} else {
				System.out.println("Unexpected response status: " + status);
			}
		} catch (IOException | UnsupportedOperationException e) {
			e.printStackTrace();
		}
		return responseBody;
	}

	/**
	 * Get the deserialized response and store it in the queue
	 */
	public void retrieveData() {
		String rawData = this.retrieveRawData();

		Gson gson = new GsonBuilder().create();
		// create JSON with the whole reply
		JsonObject jsonObject = gson.fromJson(rawData, JsonElement.class).getAsJsonObject();

		// extract summary object
		// JsonObject summary = jsonObject.get("summary").getAsJsonObject();

		// extract resources array
		JsonArray resources = jsonObject.get("resources").getAsJsonArray();

		// deserialize array of objects
		T[] observations = gson.fromJson(resources, this.getObservationsArrayClass());

		for (T observation : observations) {
			int sensorID = observation.getSensorID();
			String timestamp = observation.getTimestamp();

			if (this.getLastModified().get(sensorID).compareTo(timestamp) != -1)
				continue; // skip old or repeated values
			if (!this.getObservations().offer(observation))
				break; // queue is full!

			this.getLastModified().put(sensorID, timestamp);
		}
	}

	/**
	 * Create thread and execute requests in the background
	 */
	public void connect() {
		Runnable task = () -> {
			retrieveData();
		};
		executor.scheduleWithFixedDelay(task, 0, getUpdateFrequency(), TimeUnit.SECONDS);
	}

	/**
	 * Kill all running threads and disconnect
	 */
	@Override
	public void close() {
		ScheduledExecutorService executor = this.getExecutor();
		try {
			// Attempting to shutdown the executor
			executor.shutdown();
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// Force shutdown if any issue
			if (!executor.isTerminated()) {
				executor.shutdownNow();
			}
		}
	}
}
