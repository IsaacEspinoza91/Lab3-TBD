package com.tbd.DeliveryMedicamentos.DTO;

public class RutaSecuenciaDTO {
    private String rutaCodificada;
    private int frecuencia;

    public RutaSecuenciaDTO(String rutaCodificada, int frecuencia) {
        this.rutaCodificada = rutaCodificada;
        this.frecuencia = frecuencia;
    }

    public String getRutaCodificada() {
        return rutaCodificada;
    }

    public int getFrecuencia() {
        return frecuencia;
    }
}

