package com.tbd.DeliveryMedicamentos.DTO;

import java.sql.Date;
import java.util.List;

public class PedidosDTO {
    private Date fecha;
    private boolean urgencia;
    private int total_pagado;
    private String estado_entrega;
    private Date fecha_entrega;
    private int cliente_id;
    private int medio_pago_id;
    private int farmacia_id;
    private int repartidor_id;
    private List<Double[]> coordenadas; // Lista de puntos [lat, lng]

    public PedidosDTO() {}

    public PedidosDTO(Date fecha, boolean urgencia, int totalPagado, String estadoEntrega, Date fechaEntrega,
                      int clienteId, int medioPagoId, int farmaciaId, int repartidorId, List<Double[]> coordenadas) {
        this.fecha = fecha;
        this.urgencia = urgencia;
        this.total_pagado = totalPagado;
        this.estado_entrega = estadoEntrega;
        this.fecha_entrega = fechaEntrega;
        this.cliente_id = clienteId;
        this.medio_pago_id = medioPagoId;
        this.farmacia_id = farmaciaId;
        this.repartidor_id = repartidorId;
        this.coordenadas = coordenadas;
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

    public List<Double[]> getCoordenadas() {
        return coordenadas;
    }

    //Setter
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

    public void setCoordenadas(List<Double[]> coordenadas) {
        this.coordenadas = coordenadas;
    }
}
