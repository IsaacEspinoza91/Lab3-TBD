package com.tbd.DeliveryMedicamentos.DTO;

public class ResumenPedidoClienteDTO {
    private int cliente_id;
    private String nombre;
    private String apellido;
    private String email;
    private int cantidad_pedidos;
    private int monto_total;

    // Constructor vacío
    public ResumenPedidoClienteDTO() {}

    // Getters y Setters
    public int getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCantidad_pedidos() {
        return cantidad_pedidos;
    }

    public void setCantidad_pedidos(int cantidad_pedidos) {
        this.cantidad_pedidos = cantidad_pedidos;
    }

    public int getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(int monto_total) {
        this.monto_total = monto_total;
    }
}
