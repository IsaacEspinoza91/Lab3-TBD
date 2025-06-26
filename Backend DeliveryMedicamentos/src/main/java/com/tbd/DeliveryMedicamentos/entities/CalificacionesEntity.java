package com.tbd.DeliveryMedicamentos.entities;

public class CalificacionesEntity {

    private int id;
    private String puntuacion;
    private int estrellas;
    private int cliente_id;
    private int repartidor_id;

    // Constructores
    public CalificacionesEntity() {}

    public CalificacionesEntity(int id, String puntuacion, int estrellas, int clienteId, int repartidorId) {
        this.id = id;
        this.puntuacion = puntuacion;
        this.estrellas = estrellas;
        this.cliente_id = clienteId;
        this.repartidor_id = repartidorId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getPuntuacion() {
        return puntuacion;
    }

    public int getEstrellas() {
        return estrellas;
    }

    public int getCliente_id() {
        return cliente_id;
    }

    public int getRepartidor_id() {
        return repartidor_id;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }

    public void setEstrellas(int estrellas) {
        this.estrellas = estrellas;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public void setRepartidor_id(int repartidor_id) {
        this.repartidor_id = repartidor_id;
    }
}
