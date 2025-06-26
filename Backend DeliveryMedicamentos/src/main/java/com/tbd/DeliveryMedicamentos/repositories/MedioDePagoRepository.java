package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.entities.MedioDePagoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedioDePagoRepository {
    private final Sql2o sql2o;

    @Autowired
    public MedioDePagoRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<MedioDePagoEntity> findAll() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Medios_de_pago").executeAndFetch(MedioDePagoEntity.class);
        }
    }

    public MedioDePagoEntity findById(Integer id) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Medios_de_pago WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(MedioDePagoEntity.class);
        }
    }

    public MedioDePagoEntity save(MedioDePagoEntity medioDePago) {
        try (Connection conn = sql2o.open()) {
            int id = (Integer) conn.createQuery("INSERT INTO Medios_de_pago(tipo) VALUES (:tipo)", true)
                    .addParameter("tipo", medioDePago.getTipo())
                    .executeUpdate()
                    .getKey();
            medioDePago.setId(id);
            return medioDePago;
        }
    }

    public void update(MedioDePagoEntity medioDePago) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("UPDATE Medios_de_pago SET tipo = :tipo WHERE id = :id")
                    .addParameter("tipo", medioDePago.getTipo())
                    .addParameter("id", medioDePago.getId())
                    .executeUpdate();
        }
    }

    public void delete(Integer id) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DELETE FROM Medios_de_pago WHERE id = :id")
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }
}