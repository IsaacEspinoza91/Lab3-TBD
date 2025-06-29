package com.tbd.DeliveryMedicamentos.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "opiniones_clientes")
public class opiniones_clientesEntity {


    @Id
    private String id;

    @Field("cliente_id")
    private String clienteId;
    @Field("empresa_id")
    private String empresaId;
    private String comentario;
    private int puntuacion;
    private Instant fecha;


    public opiniones_clientesEntity() {}
    public opiniones_clientesEntity(String id, String clienteId, String empresaId, String comentario, int puntuacion, Instant fecha) {
        this.id = id;
        this.clienteId = clienteId;
        this.empresaId = empresaId;
        this.comentario = comentario;
        this.puntuacion = puntuacion;
        this.fecha = fecha;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(String empresaId) {
        this.empresaId = empresaId;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }
}
