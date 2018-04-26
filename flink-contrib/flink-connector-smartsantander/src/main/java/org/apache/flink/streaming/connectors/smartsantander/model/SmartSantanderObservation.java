package org.apache.flink.streaming.connectors.smartsantander.model;

/**
 * Observation from the sensors in Smart Santander. Implemented by all
 * observation classes. Required to bound SmartSantanderObservationStream
 *
 * @author federicofdez
 */
public interface SmartSantanderObservation {

    /**
     * Get the unique identifier of the sensor related with the observation
     *
     * @return ID of the sensor
     */
    int getSensorID();

    /**
     * Get the timestamp associated to the observation
     *
     * @return Timestamp expressed in ISO8601 format
     */
    String getTimestamp();

}
