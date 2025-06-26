package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.DTO.FarmaciaDTO;
import com.tbd.DeliveryMedicamentos.entities.FarmaciasEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class FarmaciasRepository {

    private final Sql2o sql2o;

    @Autowired
    public FarmaciasRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<FarmaciasEntity> findAll() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT id, nombre, lugar FROM Farmacias")
                    .executeAndFetch(FarmaciasEntity.class);
        }
    }

    public FarmaciasEntity findById(Integer id) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT id, nombre, lugar FROM Farmacias WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(FarmaciasEntity.class);
        }
    }

    public FarmaciaDTO findByIdConCoordenadas(Integer id) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT nombre, lugar, ST_Y(geom) AS latitud, ST_X(geom) AS longitud FROM Farmacias WHERE id = :id")
                    .addParameter("id",id)
                    .executeAndFetchFirst(FarmaciaDTO.class);
        }
    }

    public FarmaciasEntity save(FarmaciasEntity farmacia, Double lat, Double lgn) {
        try (Connection conn = sql2o.open()) {
            farmacia.setGeom(lat, lgn);
            int id = (Integer) conn.createQuery("INSERT INTO Farmacias(nombre, lugar, geom) VALUES (:nombre, :lugar, ST_GeomFromText(:geom, 4326))", true)
                    .addParameter("nombre", farmacia.getNombre())
                    .addParameter("lugar", farmacia.getLugar())
                    .addParameter("geom", farmacia.getGeom())
                    .executeUpdate()
                    .getKey();

            farmacia.setId(id);
            return farmacia;
        }
    }

    /* Formato
    POST http://localhost:8080/api/farmacias
    {
    "nombre": "Farmacia Central",
    "lugar": "Santiago",
    "latitud": -33.734539,
    "longitud": -70.860034
    }
     */

    public void update(FarmaciasEntity farmacia) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("UPDATE Farmacias SET nombre = :nombre, lugar = :lugar WHERE id = :id")
                    .addParameter("nombre", farmacia.getNombre())
                    .addParameter("lugar", farmacia.getLugar())
                    .addParameter("id", farmacia.getId())
                    .executeUpdate();
        }
    }

    public void delete(Integer id) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DELETE FROM Farmacias WHERE id = :id")
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    public long count() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT COUNT(*) FROM Farmacias")
                    .executeScalar(Long.class);
        }
    }

    public List<Map<String, Object>> obtenerFarmaciasMayorVolumenEntregado() {
        String sql = "SELECT * FROM farmacias_volumen_entregas_exitosas";
        return sql2o.open().createQuery(sql).executeAndFetchTable().asList();
    }
}