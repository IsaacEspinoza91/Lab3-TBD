package com.tbd.DeliveryMedicamentos.entities;

public class PedidosRepartidoresEntity {
    private int pedidoId;
    private int repartidor_id;

    // Constructores
    public PedidosRepartidoresEntity() {
    }

    public PedidosRepartidoresEntity(int pedidoId, int repartidor_id) {
        this.pedidoId = pedidoId;
        this.repartidor_id = repartidor_id;
    }

    // Getters y Setters
    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public int getRepartidor_id() {
        return repartidor_id;
    }

    public void setRepartidor_id(int repartidor_id) {
        this.repartidor_id = repartidor_id;
    }
}
