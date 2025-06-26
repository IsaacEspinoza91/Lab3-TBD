package com.tbd.DeliveryMedicamentos.DTO;

public class ZonaDensidadDTO {
    private int idZona;
    private String nombreZona;
    private long cantidadPedidos;

    // Constructor vacío
    public ZonaDensidadDTO() {}

    // Getters y Setters
    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public long getCantidadPedidos() {
        return cantidadPedidos;
    }

    public void setCantidadPedidos(long cantidadPedidos) {
        this.cantidadPedidos = cantidadPedidos;
    }
}
