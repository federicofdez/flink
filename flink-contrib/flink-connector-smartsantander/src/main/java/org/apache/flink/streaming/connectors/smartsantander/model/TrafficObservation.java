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
