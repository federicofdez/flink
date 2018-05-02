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

/**
 * Observation from fixed sensors that measure different parameters in the environment.
 */
public class EnvironmentObservation implements SmartSantanderObservation {

	// metadata
	@SerializedName("dc:identifier")
	private int sensorID;
	@SerializedName("dc:modified")
	private String timestamp;
	@SerializedName("ayto:latitude")
	private double latitude;
	@SerializedName("ayto:longitude")
	private double longitude;

	/**
	 * Measured noise expressed in DB.
	 */
	@SerializedName("ayto:noise")
	private double noise;
	/**
	 * Measured temperature, expressed in degrees Celsius (ºC).
	 */
	@SerializedName("ayto:temperature")
	private double temperature;

	/**
	 * Measured light intensity, expressed in lumens (lm).
	 */
	@SerializedName("ayto:light")
	private double lightIntesity;

	public EnvironmentObservation() {
		this(0, null, 0, 0, 0, 0, 0);
	}

	public EnvironmentObservation(int sensorID, String timestamp, double latitude, double longitude, double noise, double temperature, double lightIntesity) {
		this.sensorID = sensorID;
		this.timestamp = timestamp;
		this.latitude = latitude;
		this.longitude = longitude;
		this.noise = noise;
		this.temperature = temperature;
		this.lightIntesity = lightIntesity;
	}

	@Override
	public int getSensorID() {
		return sensorID;
	}

	public void setSensorID(int sensorID) {
		this.sensorID = sensorID;
	}

	@Override
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getNoise() {
		return noise;
	}

	public void setNoise(double noise) {
		this.noise = noise;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getLightIntesity() {
		return lightIntesity;
	}

	public void setLightIntesity(double lightIntesity) {
		this.lightIntesity = lightIntesity;
	}
}
