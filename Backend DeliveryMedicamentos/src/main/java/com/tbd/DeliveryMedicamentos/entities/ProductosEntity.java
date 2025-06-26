package com.tbd.DeliveryMedicamentos.entities;

public class ProductosEntity {
    private int id;
    private String nombre;
    private int precio;
    private int stock;
    private boolean requiere_receta;

    // Constructores
    public ProductosEntity() {}

    public ProductosEntity(int id, String nombre, int precio, int stock, boolean requiere_receta) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.requiere_receta = requiere_receta;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public boolean getRequiere_receta() {
        return requiere_receta;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setRequiere_receta(boolean requiere_receta) {
        this.requiere_receta = requiere_receta;
    }
}
