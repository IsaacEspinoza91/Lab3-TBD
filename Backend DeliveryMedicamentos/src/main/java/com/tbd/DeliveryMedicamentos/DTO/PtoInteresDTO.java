package com.tbd.DeliveryMedicamentos.DTO;

public class PtoInteresDTO {
    private int id;
    private String nombre;
    private String lugar;
    private Double latitud;
    private Double longitud;

    public PtoInteresDTO() {}

    public PtoInteresDTO(int id, String nombre, String lugar, Double latitud, Double longitud) {
        this.id = id;
        this.nombre = nombre;
        this.lugar = lugar;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

