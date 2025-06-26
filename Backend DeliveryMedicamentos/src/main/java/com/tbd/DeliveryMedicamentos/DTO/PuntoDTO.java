package com.tbd.DeliveryMedicamentos.DTO;

public class PuntoDTO {
    private String nombre;
    private int idFarmacia;
    private Double latitud;
    private Double longitud;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdFarmacia() {
        return idFarmacia;
    }

    public void setIdFarmacia(int IdFarmacia) {
        this.idFarmacia = IdFarmacia;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
