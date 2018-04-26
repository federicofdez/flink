package org.apache.flink.streaming.connectors.smartsantander;

import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.connectors.smartsantander.model.SmartSantanderObservation;

import java.util.concurrent.TimeUnit;

/**
 * Data source that can be used in Flink jobs and retrieves data from the sensors deployed
 * in Smart Santander
 *
 * @author federicofdez
 */
public class SmartSantanderSource<T extends SmartSantanderObservation> extends RichSourceFunction<T> {

	/**
	 * Endpoint of the API
	 */
	private final SmartSantanderAPIEndpoints endpoint;
	/**
	 * Frequency of updates expressed in seconds
	 */
	private final int updateFrequency;
	/**
	 * Type of the observations array, necessary to perform deserialization
	 */
	private final Class<T[]> observationsArrayClass;

	private volatile boolean isRunning = true;

	public SmartSantanderSource(Class<T[]> observationsArrayClass, SmartSantanderAPIEndpoints endpoint, int updateFrequency) {
		this.endpoint = endpoint;
		this.updateFrequency = updateFrequency;
		this.observationsArrayClass = observationsArrayClass;
	}

	@Override
	public void run(SourceContext<T> ctx) throws Exception {
		try (SmartSantanderObservationStream<T> stream = new SmartSantanderObservationStream<>(observationsArrayClass, endpoint, updateFrequency)) {
			// Open connection
			stream.connect();

			while (isRunning) {
				// Query for the next observation event
				T event = stream.getObservations().poll(100, TimeUnit.MILLISECONDS);

				if (event != null)
					ctx.collect(event);
			}
		}
	}

	@Override
	public void cancel() {
		isRunning = false;
	}

}
