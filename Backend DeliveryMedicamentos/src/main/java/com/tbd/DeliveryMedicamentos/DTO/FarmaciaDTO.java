package com.tbd.DeliveryMedicamentos.DTO;

public class FarmaciaDTO {

    private String nombre;
    private String lugar;
    private Double latitud;
    private Double longitud;

    public FarmaciaDTO() {}

    public FarmaciaDTO(String nombre, String lugar, Double latitud, Double longitud) {
        this.nombre = nombre;
        this.lugar = lugar;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
