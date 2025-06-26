package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.DTO.ZonaDensidadDTO;
import com.tbd.DeliveryMedicamentos.entities.ZonasEntity;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ZonasRepository {

    private final Sql2o sql2o;

    @Autowired
    public ZonasRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<ZonasEntity> findAll() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM zonas")
                    .executeAndFetch(ZonasEntity.class);
        }
    }

    public Optional<ZonasEntity> findById(int id) {
        try (Connection conn = sql2o.open()) {
            ZonasEntity zona = conn.createQuery("SELECT * FROM zonas WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(ZonasEntity.class);
            return Optional.ofNullable(zona);
        }
    }

    public ZonasEntity save(ZonasEntity zona) {
        String sql = "INSERT INTO zonas_cobertura (nombre, geom) VALUES (:nombre, ST_GeomFromText(:geom, 4326)) RETURNING id, nombre, geom";
        System.out.println("Geom enviado: " + zona.getGeom());

        try (Connection conn = sql2o.open()) {
            int id = (Integer) conn.createQuery(sql, true) // <== Agrega 'true' para devolver claves generadas
                    .addParameter("nombre", zona.getNombre())
                    .addParameter("geom", zona.getGeom())
                    .executeUpdate()
                    .getKey(); // Obtiene el ID generado

            return new ZonasEntity(id, zona.getNombre(), zona.getGeom());
        }
    }

    /*Formato
    POST http://localhost:8080/api/zonas
    {
  "nombre": "Zona PRUEBA",
  "geom": "POLYGON((-70.6506 -33.4372, -70.6510 -33.4375, -70.6490 -33.4382, -70.6506 -33.4372))"
    }
     */

    public boolean update(int id, ZonasEntity zona) {
        String sql = "UPDATE zonas SET nombre = :nombre, geom = ST_GeomFromText(:geom, 4326) WHERE id = :id";
        try (Connection conn = sql2o.open()) {
            int updated = conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("nombre", zona.getNombre())
                    .addParameter("geom", zona.getGeom())
                    .executeUpdate()
                    .getResult();
            return updated > 0;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = sql2o.open()) {
            int deleted = conn.createQuery("DELETE FROM zonas WHERE id = :id")
                    .addParameter("id", id)
                    .executeUpdate()
                    .getResult();
            return deleted > 0;
        }
    }

    public List<ZonaDensidadDTO> findZonasConAltaDensidadPedidos() {
        String sql = "SELECT " +
                "    zc.ID AS idZona, " +
                "    zc.nombre AS nombreZona, " +
                "    COUNT(p.ID) AS cantidadPedidos " +
                "FROM " +
                "    Zonas_cobertura zc " +
                "JOIN " +
                "    Pedidos p ON ST_Contains(zc.geom, ST_StartPoint(p.ruta_estimada)) " +
                "WHERE " +
                "    p.ruta_estimada IS NOT NULL " +
                "GROUP BY " +
                "    zc.ID, zc.nombre " +
                "ORDER BY " +
                "    cantidadPedidos DESC;";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(ZonaDensidadDTO.class);
        }
    }
}
