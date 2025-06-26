package com.tbd.DeliveryMedicamentos.entities;

public class RepartidorEntity {
    private int usuario_id; // FK a Usuarios
    private String tipo_vehiculo; // AUTO, MOTO, BICICLETA, CAMIONETA

    // Constructores
    public RepartidorEntity() {}

    public RepartidorEntity(int usuario_id, String tipo_vehiculo) {
        this.usuario_id = usuario_id;
        this.tipo_vehiculo = tipo_vehiculo;
    }

    // Getters y Setters
    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getTipo_vehiculo() {
        return tipo_vehiculo;
    }

    public void setTipo_vehiculo(String tipo_vehiculo) {
        this.tipo_vehiculo = tipo_vehiculo;
    }
}