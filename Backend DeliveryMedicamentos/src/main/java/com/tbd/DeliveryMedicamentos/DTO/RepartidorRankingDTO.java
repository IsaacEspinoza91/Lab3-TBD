package com.tbd.DeliveryMedicamentos.DTO;

public class RepartidorRankingDTO {
    private String nombre;
    private String apellido;
    private Double puntuacion;

    public RepartidorRankingDTO(String nombre, String apellido, Double puntuacion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.puntuacion = puntuacion;
    }

    // Getters and Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Double puntuacion) {
        this.puntuacion = puntuacion;
    }
}

