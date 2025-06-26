package com.tbd.DeliveryMedicamentos.entities;

public class AdministradorEntity {
    private int usuario_id; // FK a Usuarios
    private int nivel_acceso;
    private String departamento;

    // Constructores
    public AdministradorEntity() {}

    public AdministradorEntity(int usuario_id, int nivel_acceso, String departamento) {
        this.usuario_id = usuario_id;
        this.nivel_acceso = nivel_acceso;
        this.departamento = departamento;
    }

    // Getters y Setters
    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public int getNivel_acceso() {
        return nivel_acceso;
    }

    public void setNivel_acceso(int nivel_acceso) {
        this.nivel_acceso = nivel_acceso;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}