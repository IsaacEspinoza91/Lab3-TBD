package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.entities.CalificacionesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CalificacionesRepository {
    private final Sql2o sql2o;

    @Autowired
    public CalificacionesRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<CalificacionesEntity> findAll() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Calificaciones").executeAndFetch(CalificacionesEntity.class);
        }
    }

    public CalificacionesEntity findById(Integer id) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Calificaciones WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(CalificacionesEntity.class);
        }
    }

    public CalificacionesEntity save(CalificacionesEntity calificacion) {
        try (Connection conn = sql2o.open()) {
            int id = (Integer) conn.createQuery("INSERT INTO Calificaciones(puntuacion, estrellas, cliente_id, repartidor_id) " +
                            "VALUES (:puntuacion, :estrellas, :clienteId, :repartidorId)", true)
                    .addParameter("puntuacion", calificacion.getPuntuacion())
                    .addParameter("estrellas", calificacion.getEstrellas())
                    .addParameter("clienteId", calificacion.getCliente_id())
                    .addParameter("repartidorId", calificacion.getRepartidor_id())
                    .executeUpdate()
                    .getKey();
            calificacion.setId(id);
            return calificacion;
        }
    }

    public void update(CalificacionesEntity calificacion) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("UPDATE Calificaciones SET puntuacion = :puntuacion, estrellas = :estrellas, " +
                            "cliente_id = :clienteId, repartidor_id = :repartidorId WHERE id = :id")
                    .addParameter("puntuacion", calificacion.getPuntuacion())
                    .addParameter("estrellas", calificacion.getEstrellas())
                    .addParameter("clienteId", calificacion.getCliente_id())
                    .addParameter("repartidorId", calificacion.getRepartidor_id())
                    .addParameter("id", calificacion.getId())
                    .executeUpdate();
        }
    }

    public void delete(Integer id) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DELETE FROM Calificaciones WHERE id = :id")
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }
}