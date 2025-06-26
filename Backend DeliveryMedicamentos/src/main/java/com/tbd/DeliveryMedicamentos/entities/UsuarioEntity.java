package com.tbd.DeliveryMedicamentos.entities;

public class UsuarioEntity {
    private int id;
    private String rut;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String telefono;
    private String tipo; // CLIENTE, REPARTIDOR, ADMIN
    private String geom;

    // Constructores
    public UsuarioEntity() {}

    public UsuarioEntity(int id, String rut, String nombre, String apellido, String email, String password, String telefono, String tipo, Double latitud, Double longitud) {
        this.id = id;
        this.rut = rut;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.tipo = tipo;
        setGeom(latitud, longitud);
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGeom() {
        return geom;
    }

    // Crear ubicacion segun latitud y longitud
    public void setGeom(Double latitud, Double longitud) {
        if (latitud != null && longitud != null) {
            // PostGIS usa orden X,Y (lng,lat). Ademas se reemplazan las comas por puntos para el formato
            this.geom = String.format("POINT(%f %f)", longitud, latitud).replace(",", ".");
        }
    }

}