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
 * Observation from mobile sensors that measure different gas levels in air
 *
 * @author federicofdez
 */
public class AirQualityObservation implements SmartSantanderObservation {

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
     * Level of NO2 expressed in ug/m3
     */
    @SerializedName("ayto:NO2")
    private double NO2Level;
    /**
     * Level of CO expressed in mg/m3
     */
    @SerializedName("ayto:CO")
    private double COLevel;
    /**
     * Level of ozone expressed in ug/m3
     */
    @SerializedName("ayto:ozone")
    private double ozoneLevel;
    /**
     * Measured temperature, expressed in degrees Celsius (ºC)
     */
    @SerializedName("ayto:temperature")
    private double temperature;

    public AirQualityObservation() {
        this(0, null, 0, 0, 0, 0, 0, 0);
    }

    public AirQualityObservation(int sensorID, String timestamp, double latitude, double longitude, double NO2Level, double COLevel, double ozoneLevel, double temperature) {
        this.sensorID = sensorID;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.NO2Level = NO2Level;
        this.COLevel = COLevel;
        this.ozoneLevel = ozoneLevel;
        this.temperature = temperature;
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

    public double getNO2Level() {
        return NO2Level;
    }

    public void setNO2Level(double NO2Level) {
        this.NO2Level = NO2Level;
    }

    public double getCOLevel() {
        return COLevel;
    }

    public void setCOLevel(double COLevel) {
        this.COLevel = COLevel;
    }

    public double getOzoneLevel() {
        return ozoneLevel;
    }

    public void setOzoneLevel(double ozoneLevel) {
        this.ozoneLevel = ozoneLevel;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
