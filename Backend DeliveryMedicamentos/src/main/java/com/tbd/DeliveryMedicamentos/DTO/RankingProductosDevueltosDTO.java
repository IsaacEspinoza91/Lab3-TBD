package com.tbd.DeliveryMedicamentos.DTO;

public class RankingProductosDevueltosDTO {
    private Long producto_id;
    private String nombre_producto;
    private Integer veces_devuelto;
    private String porcentaje_devoluciones;

    // Constructor
    public RankingProductosDevueltosDTO() {
    }

    public RankingProductosDevueltosDTO(Long producto_id, String nombre_producto, Integer veces_devuelto, String procentajeDevoluciones) {
        this.producto_id = producto_id;
        this.nombre_producto = nombre_producto;
        this.veces_devuelto = veces_devuelto;
        this.porcentaje_devoluciones = procentajeDevoluciones;
    }

    // Gettesr y setters
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

    public Integer getVeces_devuelto() {
        return veces_devuelto;
    }

    public void setVeces_devuelto(Integer veces_devuelto) {
        this.veces_devuelto = veces_devuelto;
    }

    public String getProcentajeDevoluciones() {
        return porcentaje_devoluciones;
    }

    public void setProcentajeDevoluciones(String procentajeDevoluciones) {
        this.porcentaje_devoluciones = procentajeDevoluciones;
    }
}
