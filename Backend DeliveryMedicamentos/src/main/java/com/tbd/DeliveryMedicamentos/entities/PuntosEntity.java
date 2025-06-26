package com.tbd.DeliveryMedicamentos.entities;

public class PuntosEntity {
    private int id;
    private String nombre;
    private int idFarmacia;
    private String geom;

    public PuntosEntity(String nombre, int idFarmacia, Double latitud, Double longitud) {
        this.nombre = nombre;
        this.idFarmacia = idFarmacia;
        setGeom(latitud, longitud);
    }

    public PuntosEntity() {
    }

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

    public int getidFarmacia() {
        return idFarmacia;
    }

    public void setidFarmacia(int idFarmacia) {
        this.idFarmacia = idFarmacia;
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
