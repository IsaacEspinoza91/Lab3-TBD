package com.tbd.DeliveryMedicamentos.entities;

public class FarmaciasEntity {
    private int id;
    private String nombre;
    private String lugar;
    private String geom;

    // Constructores
    public FarmaciasEntity() {}

    public FarmaciasEntity(int id, String nombre, String lugar, Double latitud, Double longitud) {
        this.id = id;
        this.nombre = nombre;
        this.lugar = lugar;
        setGeom(latitud, longitud);
    }

    // Getters y Setter
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getLugar() {
        return lugar;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
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
