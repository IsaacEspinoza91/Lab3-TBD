package com.tbd.DeliveryMedicamentos.DTO;

public class ClienteDetalladoDTO {
    private int usuarioId;
    private String nombre;
    private String email;
    private String direccion;

    public ClienteDetalladoDTO() {}

    // Getters y Setters

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}

