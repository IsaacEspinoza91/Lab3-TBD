package com.tbd.DeliveryMedicamentos.DTO;

import com.tbd.DeliveryMedicamentos.entities.PedidosEntity;

import java.util.List;

public class PedidoConDetallesDTO {
    private PedidosEntity pedido;
    private List<DetallePedidoDTO> detalles;

    // Getters y setters
    public PedidosEntity getPedido() { return pedido; }
    public void setPedido(PedidosEntity pedido) { this.pedido = pedido; }

    public List<DetallePedidoDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedidoDTO> detalles) { this.detalles = detalles; }
}
