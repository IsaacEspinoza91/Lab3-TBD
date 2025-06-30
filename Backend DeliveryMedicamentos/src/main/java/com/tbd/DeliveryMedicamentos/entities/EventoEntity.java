package com.tbd.DeliveryMedicamentos.entities;

public class EventoEntity {
    private String estado;
    private String timestamp;

    public EventoEntity() {
    }

    public EventoEntity(String estado, String timestamp) {
        this.estado = estado;
        this.timestamp = timestamp;
    }

    // Getters y Setters
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}

