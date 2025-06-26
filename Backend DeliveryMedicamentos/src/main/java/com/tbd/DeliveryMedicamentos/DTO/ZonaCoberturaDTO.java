package com.tbd.DeliveryMedicamentos.DTO;

public class ZonaCoberturaDTO {
    private int idUsuario;
    private int idZonaCobertura;
    private String nombreZonaCobertura;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdZonaCobertura() {
        return idZonaCobertura;
    }

    public void setIdZonaCobertura(int idZonaCobertura) {
        this.idZonaCobertura = idZonaCobertura;
    }

    public String getNombreZonaCobertura() {
        return nombreZonaCobertura;
    }

    public void setNombreZonaCobertura(String nombreZonaCobertura) {
        this.nombreZonaCobertura = nombreZonaCobertura;
    }
}
