package com.tbd.DeliveryMedicamentos.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "logs_pedidos")
public class logs_pedidosEntity {

    @Id
    private String _id;

    @Field("pedido_id")
    private String pedidoId;
    private List<EventoEntity> eventos;

    public logs_pedidosEntity() {
    }

    public logs_pedidosEntity(String pedidoId, List<EventoEntity> eventos) {
        this.pedidoId = pedidoId;
        this.eventos = eventos;
    }

    // Getters y Setters
    public String getPedidoId() { return pedidoId; }
    public void setPedido_id(String pedido_id) { this.pedidoId = pedido_id; }

    public List<EventoEntity> getEventos() { return eventos; }
    public void setEventos(List<EventoEntity> eventos) { this.eventos = eventos; }


}
