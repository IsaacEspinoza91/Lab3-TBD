package com.tbd.DeliveryMedicamentos.DTO;

public class RepartidorDistanciaMensualDTO {
    private String repartidor_Nombre;
    private String repartidor_Apellido;
    private String mes_Entrega;
    private double distancia_Total_Metros;

    // Constructor vacío (necesario para sql2o)
    public RepartidorDistanciaMensualDTO() {
    }

    // Getters y Setters
    public String getRepartidor_Nombre() {
        return repartidor_Nombre;
    }

    public void setRepartidor_Nombre(String repartidor_Nombre) {
        this.repartidor_Nombre = repartidor_Nombre;
    }

    public String getRepartidor_Apellido() {
        return repartidor_Apellido;
    }

    public void setRepartidor_Apellido(String repartidor_Apellido) {
        this.repartidor_Apellido = repartidor_Apellido;
    }

    public String getMes_Entrega() {
        return mes_Entrega;
    }

    public void setMes_Entrega(String mes_Entrega) {
        this.mes_Entrega = mes_Entrega;
    }

    public double getDistancia_Total_Metros() {
        return distancia_Total_Metros;
    }

    public void setDistancia_Total_Metros(double distancia_Total_Metros) {
        this.distancia_Total_Metros = distancia_Total_Metros;
    }
}