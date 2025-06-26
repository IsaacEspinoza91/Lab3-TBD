package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.entities.FarmaciasProductosEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FarmaciasProductosRepository {
    private final Sql2o sql2o;

    @Autowired
    public FarmaciasProductosRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<FarmaciasProductosEntity> findAll() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Farmacias_Productos").executeAndFetch(FarmaciasProductosEntity.class);
        }
    }

    public List<FarmaciasProductosEntity> findByFarmaciaId(int farmaciaId) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Farmacias_Productos WHERE farmacia_id = :farmaciaId")
                    .addParameter("farmaciaId", farmaciaId)
                    .executeAndFetch(FarmaciasProductosEntity.class);
        }
    }

    public List<FarmaciasProductosEntity> findByProductoId(int productoId) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Farmacias_Productos WHERE producto_id = :productoId")
                    .addParameter("productoId", productoId)
                    .executeAndFetch(FarmaciasProductosEntity.class);
        }
    }

    public FarmaciasProductosEntity findByFarmaciaProductoId(Integer farmaciaId, Integer productoId) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Farmacias_Productos WHERE farmacia_id = :farmaciaId AND producto_id = :productoId")
                    .addParameter("farmaciaId", farmaciaId)
                    .addParameter("productoId", productoId)
                    .executeAndFetchFirst(FarmaciasProductosEntity.class);
        }
    }

    public FarmaciasProductosEntity save(FarmaciasProductosEntity farmaciaProducto) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("INSERT INTO Farmacias_Productos(farmacia_id, producto_id) " +
                            "VALUES (:farmaciaId, :productoId)")
                    .addParameter("farmaciaId", farmaciaProducto.getFarmacia_id())
                    .addParameter("productoId", farmaciaProducto.getProducto_id())
                    .executeUpdate();
            return farmaciaProducto;
        }
    }

    // No tiene sentido hacer update de tabla intermedia con solo id compuesta
    /*public void update(FarmaciasProductosEntity farmaciaProducto) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("UPDATE Farmacias_Productos" +
                            "WHERE farmacia_id = :farmaciaId AND producto_id = :productoId")
                    .addParameter("farmaciaId", farmaciaProducto.getFarmacia_id())
                    .addParameter("productoId", farmaciaProducto.getProducto_id())
                    .executeUpdate();
        }
    }*/


    public void delete(FarmaciasProductosEntity farmaciaProducto) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DELETE FROM Farmacias_Productos WHERE farmacia_id = :farmaciaId AND producto_id = :productoId")
                    .addParameter("farmaciaId", farmaciaProducto.getFarmacia_id())
                    .addParameter("productoId", farmaciaProducto.getProducto_id())
                    .executeUpdate();
        }
    }
}