package com.tbd.DeliveryMedicamentos.DTO;

public class RutasZonasCruzadasDTO {
    private int pedidoId;
    private String zonasCruzadas;
    private int cantidadZonas;

    // Getters y Setters
    public int getPedidoId() { return pedidoId; }
    public void setPedidoId(int pedidoId) { this.pedidoId = pedidoId; }

    public String getZonasCruzadas() { return zonasCruzadas; }
    public void setZonasCruzadas(String zonasCruzadas) { this.zonasCruzadas = zonasCruzadas; }

    public int getCantidadZonas() { return cantidadZonas; }
    public void setCantidadZonas(int cantidadZonas) { this.cantidadZonas = cantidadZonas; }
}
