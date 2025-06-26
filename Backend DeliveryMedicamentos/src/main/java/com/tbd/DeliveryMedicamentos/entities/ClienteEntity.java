package com.tbd.DeliveryMedicamentos.entities;

public class ClienteEntity {
    private int usuario_id; // FK a Usuarios
    private String direccion;

    // Constructores
    public ClienteEntity() {}

    public ClienteEntity(int usuario_id, String direccion) {
        this.usuario_id = usuario_id;
        this.direccion = direccion;
    }

    // Getters y Setters
    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Metodo toString, util para hacer debugging
    /*
    @Override
    public String toString() {
        return "ClienteEntity{" +
                "id=" + id +
                ", rut='" + rut + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", direccion='" + direccion + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }*/

}