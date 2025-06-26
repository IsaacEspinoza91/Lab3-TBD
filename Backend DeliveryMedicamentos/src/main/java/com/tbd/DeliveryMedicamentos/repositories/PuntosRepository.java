package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.DTO.PuntoConCoordenadasDTO;
import com.tbd.DeliveryMedicamentos.DTO.PuntoConDistanciaDTO;
import com.tbd.DeliveryMedicamentos.entities.PuntosEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PuntosRepository {

    private final Sql2o sql2o;

    @Autowired
    public PuntosRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    // Guardar punto
    public PuntosEntity save(PuntosEntity punto, Double lat, Double lng) {
        try (Connection conn = sql2o.open()) {
            punto.setGeom(lat, lng);
            int id = conn.createQuery("INSERT INTO punto_de_entrega (nombre, Farmacia_ID, geom) VALUES (:nombre, :Farmacia_ID, ST_GeomFromText(:geom, 4326))", true)
                    .addParameter("nombre", punto.getNombre())
                    .addParameter("Farmacia_ID", punto.getidFarmacia())
                    .addParameter("geom", punto.getGeom())
                    .executeUpdate()
                    .getKey(Integer.class);
            punto.setId(id);
            return punto;
        }
    }

    /* Formato
    POST http://localhost:8080/api/puntos
    {
      "nombre": "Punto Providencia",
      "idFarmacia": 1,
      "latitud": -33.426209,
      "longitud": -70.617221
    }

     */

    // Obtener todos los puntos
    public List<PuntosEntity> findAll() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT id, nombre, Farmacia_ID AS idFarmacia, ST_AsText(geom) AS geom FROM punto_de_entrega")
                    .executeAndFetch(PuntosEntity.class);
        }
    }

    // Obtener todos los puntos y las coordenadas separadas
    // Formato repuesta
    //{
    //    "id": 1,
    //        "nombre": "Kiosco Centro Providencia",
    //        "idFarmacia": 1,
    //        "geom": "POINT(-70.618 -33.4385)",
    //        "latitud": -33.4385,
    //        "longitud": -70.618
    //}
    public List<PuntoConCoordenadasDTO> findAllPuntosConCoordenadas() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT id, nombre, farmacia_id AS idFarmacia, ST_AsText(geom) AS geom, ST_Y(geom) AS latitud, ST_X(geom) AS longitud FROM punto_de_entrega")
                    .executeAndFetch(PuntoConCoordenadasDTO.class);
        }
    }


    // Obtener segun id
    public Optional<PuntosEntity> findById(int id) {
        try (Connection conn = sql2o.open()) {
            PuntosEntity punto = conn.createQuery("SELECT id, nombre, Farmacia_ID AS idFarmacia, ST_AsText(geom) AS geom FROM punto_de_entrega WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(PuntosEntity.class);
            return Optional.ofNullable(punto);
        }
    }

    public Optional<PuntoConCoordenadasDTO> findPuntoConCoordenadas(int id) {
        try (Connection conn = sql2o.open()) {
            PuntoConCoordenadasDTO punto = conn.createQuery("SELECT id, nombre, farmacia_id AS idFarmacia, ST_AsText(geom) AS geom, ST_Y(geom) AS latitud, ST_X(geom) AS longitud FROM punto_de_entrega WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(PuntoConCoordenadasDTO.class);
            return Optional.ofNullable(punto);
        }
    }

    // Actualizar punto
    public boolean update(int id, PuntosEntity punto, Double lat, Double lng) {
        try (Connection conn = sql2o.open()) {
            punto.setGeom(lat, lng);
            int result = conn.createQuery("UPDATE punto_de_entrega SET nombre = :nombre, Farmacia_ID = :Farmacia_ID, geom = ST_GeomFromText(:geom, 4326) WHERE id = :id")
                    .addParameter("id", id)
                    .addParameter("nombre", punto.getNombre())
                    .addParameter("Farmacia_ID", punto.getidFarmacia())
                    .addParameter("geom", punto.getGeom())
                    .executeUpdate()
                    .getResult();
            return result > 0;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = sql2o.open()) {
            int result = conn.createQuery("DELETE FROM punto_de_entrega WHERE id = :id")
                    .addParameter("id", id)
                    .executeUpdate()
                    .getResult();
            return result > 0;
        }
    }

    // obtener los 5 puntos mas cercanos de cada farmacia
    public List<PuntoConDistanciaDTO> findTop5PuntosCercanosPorFarmacia() {
        String sql = """
        SELECT farmaciaId, farmaciaNombre, puntoEntregaId, puntoEntregaNombre, distanciaMetros, latitud, longitud
        FROM (
            SELECT
                f.id AS farmaciaId,
                f.nombre AS farmaciaNombre,
                p.id AS puntoEntregaId,
                p.nombre AS puntoEntregaNombre,
                ST_DistanceSphere(f.geom, p.geom) AS distanciaMetros,
                ST_Y(p.geom) AS latitud,
                ST_X(p.geom) AS longitud,
                ROW_NUMBER() OVER (PARTITION BY f.id ORDER BY ST_DistanceSphere(f.geom, p.geom)) AS rn
            FROM Farmacias f
            JOIN Punto_de_entrega p ON p.farmacia_id = f.id
        ) sub
        WHERE rn <= 5
        ORDER BY farmaciaId, distanciaMetros;
    """;

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .executeAndFetch(PuntoConDistanciaDTO.class);
        }
    }

    // Obtener punto mas lejando de cada farmacia
    public List<PuntoConDistanciaDTO> findPuntosMasLejanosPorFarmacia() {
        String sql = """
        SELECT DISTINCT ON (f.id)
            f.id AS farmaciaId,
            f.Nombre AS farmaciaNombre,
            p.id AS puntoEntregaId,
            p.Nombre AS puntoEntregaNombre,
            ST_DistanceSphere(f.geom, p.geom) AS distanciaMetros,
            ST_Y(p.geom) AS latitud, ST_X(p.geom) AS longitud 
        FROM Farmacias f
        JOIN Punto_de_entrega p ON p.farmacia_id = f.id
        ORDER BY f.id, ST_DistanceSphere(f.geom, p.geom) DESC; 
    """;

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .executeAndFetch(PuntoConDistanciaDTO.class);
        }
    }
}
