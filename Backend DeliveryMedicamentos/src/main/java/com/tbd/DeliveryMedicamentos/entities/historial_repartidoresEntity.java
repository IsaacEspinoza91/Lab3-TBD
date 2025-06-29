package com.tbd.DeliveryMedicamentos.entities;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "historial_repartidores")
public class historial_repartidoresEntity {

    private String repartidor_id;
    private List<RutaEntity> rutas;

    // Getters y Setters
    public String getRepartidor_id() { return repartidor_id; }
    public void setRepartidor_id(String repartidor_id) { this.repartidor_id = repartidor_id; }

    public List<RutaEntity> getRutas() { return rutas; }
    public void setRutas(List<RutaEntity> rutas) { this.rutas = rutas; }
}
