/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.streaming.connectors.smartsantander.model;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Observation from magnetic sensors that measure traffic.
 */
public class TrafficObservation implements SmartSantanderObservation {

	// metadata
	@SerializedName("ayto:idSensor")
	private final int sensorID;
	@SerializedName("dc:modified")
	private final String timestamp;
	private double latitude;
	private double longitude;

	/**
	 * Time percentage that the transit loop is occupied by a vehicle.
	 */
	@SerializedName("ayto:ocupacion")
	private final int occupation;
	/**
	 * Number of counted vehicles, expanded to vehicles per hour (vph).
	 */
	@SerializedName("ayto:intensidad")
	private final int intensity;
	/**
	 * Estimation of congestion based on occupation and intensity (on a 100-basis).
	 */
	@SerializedName("ayto:carga")
	private final int charge;

	public TrafficObservation() {
		this(null, 0, 0, 0, 0);
	}

	public TrafficObservation(String timestamp, int sensorID, int occupation, int intensity, int charge) {
		this.timestamp = timestamp;
		this.sensorID = sensorID;
		this.occupation = occupation;
		this.intensity = intensity;
		this.charge = charge;
		double[] coordinates = findCoordinates(sensorID);
		this.latitude = coordinates[0];
		this.longitude = coordinates[1];
	}

	public int getSensorID() {
		return sensorID;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public int getOccupation() {
		return occupation;
	}

	public int getIntensity() {
		return intensity;
	}

	public int getCharge() {
		return charge;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public static double[] findCoordinates(int sensorID) {
		Reader in = null;
		Iterable<CSVRecord> records = null;
		try {
			ClassLoader classLoader = TrafficObservation.class.getClassLoader();
			in = new FileReader(classLoader.getResource("trafficSensorsLocation.csv").getFile());
			records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		double[] coordinates = new double[2];
		for (CSVRecord record : records) {
			if (Integer.valueOf(record.get("sensorID")) != sensorID) {
				continue;
			}
			coordinates[0] = Double.valueOf(record.get("latitude"));
			coordinates[1] = Double.valueOf(record.get("longitude"));
			break;
		}

		return coordinates;
	}
}
