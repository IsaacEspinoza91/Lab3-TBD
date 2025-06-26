package com.tbd.DeliveryMedicamentos.entities;

public class FarmaciasProductosEntity {
    private int farmacia_id;   // FK a Farmacia
    private int producto_id;   // FK a Producto

    // Constructores
    public FarmaciasProductosEntity() {}

    public FarmaciasProductosEntity(int farmaciaId, int productoId, int stock) {
        this.farmacia_id = farmaciaId;
        this.producto_id = productoId;
    }

    // Getters
    public int getFarmacia_id() {
        return farmacia_id;
    }

    public int getProducto_id() {
        return producto_id;
    }

    // Setters
    public void setFarmacia_id(int farmacia_id) {
        this.farmacia_id = farmacia_id;
    }

    public void setProducto_id(int producto_id) {
        this.producto_id = producto_id;
    }

}
