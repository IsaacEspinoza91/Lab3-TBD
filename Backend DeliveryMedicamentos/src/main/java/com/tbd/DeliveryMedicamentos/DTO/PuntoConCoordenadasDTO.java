package com.tbd.DeliveryMedicamentos.DTO;

public class PuntoConCoordenadasDTO {
    private int id;
    private String nombre;
    private int idFarmacia;
    private String geom;
    private double latitud;
    private double longitud;

    public PuntoConCoordenadasDTO() {}

    public PuntoConCoordenadasDTO(int id, String nombre, int idFarmacia, String geom, double latitud, double longitud) {
        this.id = id;
        this.nombre = nombre;
        this.idFarmacia = idFarmacia;
        this.geom = geom;
        this.latitud = latitud;
        this.longitud = longitud;
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

    public int getIdFarmacia() {
        return idFarmacia;
    }

    public void setIdFarmacia(int idFarmacia) {
        this.idFarmacia = idFarmacia;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}