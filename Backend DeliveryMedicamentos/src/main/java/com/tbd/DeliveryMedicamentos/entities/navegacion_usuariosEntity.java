package com.tbd.DeliveryMedicamentos.entities;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "navegacion_usuarios")
public class navegacion_usuariosEntity {

    private String cliente_id;
    private List<EventoNavegacionEntity> eventos;

    // Getters y Setters
    public String getCliente_id() { return cliente_id; }
    public void setCliente_id(String cliente_id) { this.cliente_id = cliente_id; }

    public List<EventoNavegacionEntity> getEventos() { return eventos; }
    public void setEventos(List<EventoNavegacionEntity> eventos) { this.eventos = eventos; }
}
