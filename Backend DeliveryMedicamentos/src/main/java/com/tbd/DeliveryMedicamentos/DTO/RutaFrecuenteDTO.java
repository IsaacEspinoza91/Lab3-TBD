package com.tbd.DeliveryMedicamentos.DTO;

public class RutaFrecuenteDTO {
    private double lat;
    private double lng;
    private long frecuencia;

    public RutaFrecuenteDTO() {}

    public RutaFrecuenteDTO(double lat, double lng, long frecuencia) {
        this.lat = lat;
        this.lng = lng;
        this.frecuencia = frecuencia;
    }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

    public long getFrecuencia() { return frecuencia; }
    public void setFrecuencia(long frecuencia) { this.frecuencia = frecuencia; }
}
