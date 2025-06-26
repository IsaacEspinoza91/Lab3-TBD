package com.tbd.DeliveryMedicamentos.DTO;

public class PuntoConDistanciaDTO {
    private int farmaciaId;
    private String farmaciaNombre;
    private int puntoEntregaId;
    private String puntoEntregaNombre;
    private double distanciaMetros;
    private double latitud;
    private double longitud;

    // Getters y setters
    public int getFarmaciaId() { return farmaciaId; }
    public void setFarmaciaId(int farmaciaId) { this.farmaciaId = farmaciaId; }

    public String getFarmaciaNombre() { return farmaciaNombre; }
    public void setFarmaciaNombre(String farmaciaNombre) { this.farmaciaNombre = farmaciaNombre; }

    public int getPuntoEntregaId() { return puntoEntregaId; }
    public void setPuntoEntregaId(int puntoEntregaId) { this.puntoEntregaId = puntoEntregaId; }

    public String getPuntoEntregaNombre() { return puntoEntregaNombre; }
    public void setPuntoEntregaNombre(String puntoEntregaNombre) { this.puntoEntregaNombre = puntoEntregaNombre; }

    public double getDistanciaMetros() { return distanciaMetros; }
    public void setDistanciaMetros(double distanciaMetros) { this.distanciaMetros = distanciaMetros; }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}