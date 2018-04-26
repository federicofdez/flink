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
 * Observation from magnetic sensors that measure traffic
 *
 * @author federicofdez
 */
public class TrafficObservation implements SmartSantanderObservation {

	// metadata
	@SerializedName("ayto:idSensor")
	private final int sensorID;
	@SerializedName("dc:modified")
	private final String timestamp;

	/**
	 * Time percentage that the transit loop is occupied by a vehicle
	 */
	@SerializedName("ayto:ocupacion")
	private final int occupation;
	/**
	 * Number of counted vehicles, expanded to vehicles per hour (vph)
	 */
	@SerializedName("ayto:intensidad")
	private final int intensity;
	/**
	 * Estimation of congestion based on occupation and intensity (on a 100-basis)
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
}
