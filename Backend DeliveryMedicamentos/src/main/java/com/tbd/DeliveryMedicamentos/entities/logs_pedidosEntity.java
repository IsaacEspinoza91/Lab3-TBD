package com.tbd.DeliveryMedicamentos.entities;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "logs_pedidos")
public class logs_pedidosEntity {

    private String pedido_id;
    private List<EventoEntity> eventos;

    public logs_pedidosEntity(String pedido_id, List<EventoEntity> eventos) {
        this.pedido_id = pedido_id;
        this.eventos = eventos;
    }

    // Getters y Setters
    public String getPedido_id() { return pedido_id; }
    public void setPedido_id(String pedido_id) { this.pedido_id = pedido_id; }

    public List<EventoEntity> getEventos() { return eventos; }
    public void setEventos(List<EventoEntity> eventos) { this.eventos = eventos; }


}
