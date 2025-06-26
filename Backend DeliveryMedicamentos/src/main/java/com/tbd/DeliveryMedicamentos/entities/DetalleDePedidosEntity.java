package com.tbd.DeliveryMedicamentos.entities;

public class DetalleDePedidosEntity {
    private int id;
    private int pedido_id;
    private int producto_id;
    private int cantidad;

    // Constructores
    public DetalleDePedidosEntity() {}

    public DetalleDePedidosEntity(int id, int pedidoId, int productoId, int cantidad) {
        this.id = id;
        this.pedido_id = pedidoId;
        this.producto_id = productoId;
        this.cantidad = cantidad;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getPedido_id() {
        return pedido_id;
    }

    public int getProducto_id() {
        return producto_id;
    }

    public int getCantidad() {
        return cantidad;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPedido_id(int pedido_id) {
        this.pedido_id = pedido_id;
    }

    public void setProducto_id(int producto_id) {
        this.producto_id = producto_id;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
