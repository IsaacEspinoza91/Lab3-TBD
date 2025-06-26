package com.tbd.DeliveryMedicamentos.DTO;

public class ZonaUsuarioDTO {
    private String nombreCliente;
    private String nombreZona;
    private String ubicacionCliente;
    private String poligonoZona;

    public ZonaUsuarioDTO() {
    }

    public ZonaUsuarioDTO(String nombreCliente, String nombreZona, String ubicacionCliente, String poligonoZona) {
        this.nombreCliente = nombreCliente;
        this.nombreZona = nombreZona;
        this.ubicacionCliente = ubicacionCliente;
        this.poligonoZona = poligonoZona;
    }

    // Getters y Setters


    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public String getUbicacionCliente() {
        return ubicacionCliente;
    }

    public void setUbicacionCliente(String ubicacionCliente) {
        this.ubicacionCliente = ubicacionCliente;
    }

    public String getPoligonoZona() {
        return poligonoZona;
    }

    public void setPoligonoZona(String poligonoZona) {
        this.poligonoZona = poligonoZona;
    }
}
