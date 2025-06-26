package com.tbd.DeliveryMedicamentos.DTO;

public class RankingProductosCanceladosDTO {
    private Long producto_id;
    private String nombre_producto;
    private Integer veces_cancelado;
    private String porcentaje_cancelaciones;

    // Constructor
    public RankingProductosCanceladosDTO() {
    }

    public RankingProductosCanceladosDTO(Long producto_id, String nombre_producto, Integer veces_cancelado, String porcentaje_cancelaciones) {
        this.producto_id = producto_id;
        this.nombre_producto = nombre_producto;
        this.veces_cancelado = veces_cancelado;
        this.porcentaje_cancelaciones = porcentaje_cancelaciones;
    }

    // Getters y Setters
    public Long getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(Long producto_id) {
        this.producto_id = producto_id;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public Integer getVeces_cancelado() {
        return veces_cancelado;
    }

    public void setVeces_cancelado(Integer veces_cancelado) {
        this.veces_cancelado = veces_cancelado;
    }

    public String getPorcentaje_cancelaciones() {
        return porcentaje_cancelaciones;
    }

    public void setPorcentaje_cancelaciones(String porcentaje_cancelaciones) {
        this.porcentaje_cancelaciones = porcentaje_cancelaciones;
    }
}
