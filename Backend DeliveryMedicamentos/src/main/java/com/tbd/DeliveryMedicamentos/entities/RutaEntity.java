package com.tbd.DeliveryMedicamentos.entities;

public class RutaEntity {
    private double lat;
    private double lng;
    private String timestamp;

    // Getters y Setters
    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
