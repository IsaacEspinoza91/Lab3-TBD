package com.tbd.DeliveryMedicamentos.entities;

import java.sql.Date;
import java.util.List;

public class PedidosEntity {
    private int id;
    private Date fecha;
    private boolean urgencia;
    private int total_pagado;
    private String estado_entrega;
    private Date fecha_entrega;
    private int cliente_id;
    private int medio_pago_id;
    private int farmacia_id;
    private int repartidor_id;
    private String ruta_estimada; // Almacenar la ruta como WKT (LINESTRING)
    private String ruta_estimada_mls;


    // Constructores
    public PedidosEntity() {}

    public PedidosEntity(int id, Date fecha, boolean urgencia, int total_pagado, String estado_entrega, Date fecha_entrega, int cliente_id, int medio_pago_id, int farmacia_id, int repartidor_id, String rutaEstimada) {
        this.id = id;
        this.fecha = fecha;
        this.urgencia = urgencia;
        this.total_pagado = total_pagado;
        this.estado_entrega = estado_entrega;
        this.fecha_entrega = fecha_entrega;
        this.cliente_id = cliente_id;
        this.medio_pago_id = medio_pago_id;
        this.farmacia_id = farmacia_id;
        this.repartidor_id = repartidor_id;
        this.ruta_estimada = ruta_estimada;
    }

    // Getters
    public int getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public boolean getUrgencia() {
        return urgencia;
    }

    public int getTotal_pagado() {
        return total_pagado;
    }

    public String getEstado_entrega() {
        return estado_entrega;
    }

    public Date getFecha_entrega() {
        return fecha_entrega;
    }

    public int getCliente_id() {
        return cliente_id;
    }

    public int getMedio_pago_id() {
        return medio_pago_id;
    }

    public int getFarmacia_id() {
        return farmacia_id;
    }

    public int getRepartidor_id() {
        return repartidor_id;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setUrgencia(boolean urgencia) {
        this.urgencia = urgencia;
    }

    public void setTotal_pagado(int total_pagado) {
        this.total_pagado = total_pagado;
    }

    public void setEstado_entrega(String estado_entrega) {
        this.estado_entrega = estado_entrega;
    }

    public void setFecha_entrega(Date fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public void setMedio_pago_id(int medio_pago_id) {
        this.medio_pago_id = medio_pago_id;
    }

    public void setFarmacia_id(int farmacia_id) {
        this.farmacia_id = farmacia_id;
    }

    public void setRepartidor_id(int repartidor_id) {
        this.repartidor_id = repartidor_id;
    }

    public void setRutaEstimada(String ruta_estimada) {
        this.ruta_estimada = ruta_estimada;
    }

    public String getRutaEstimada() {
        return ruta_estimada;
    }

    public String getRuta_estimada_mls() {
        return ruta_estimada_mls;
    }

    public void setRuta_estimada_mls(String ruta_estimada_mls) {
        this.ruta_estimada_mls = ruta_estimada_mls;
    }

    public String getRuta_estimada() {
        return ruta_estimada;
    }

    public void setRuta_estimada(String ruta_estimada) {
        this.ruta_estimada = ruta_estimada;
    }
}
