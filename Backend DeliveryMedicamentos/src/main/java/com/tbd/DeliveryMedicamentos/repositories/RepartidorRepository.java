package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.DTO.RepartidorDistanciaMensualDTO;
import com.tbd.DeliveryMedicamentos.DTO.RepartidorRankingDTO;
import com.tbd.DeliveryMedicamentos.entities.RepartidorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RepartidorRepository {
    private final Sql2o sql2o;

    @Autowired
    public RepartidorRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<RepartidorEntity> findAll() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Repartidores").executeAndFetch(RepartidorEntity.class);
        }
    }

    public RepartidorEntity findByUsuarioId(Integer usuarioId) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Repartidores WHERE usuario_id = :usuarioId")
                    .addParameter("usuarioId", usuarioId)
                    .executeAndFetchFirst(RepartidorEntity.class);
        }
    }

    public RepartidorEntity save(RepartidorEntity repartidor) {
        String sql = "INSERT INTO Repartidores(usuario_id, tipo_vehiculo) VALUES (:usuarioId, :tipoVehiculo) RETURNING usuario_id";
        System.out.println("Datos enviados: usuario_id = " + repartidor.getUsuario_id() + ", tipo_vehiculo = " + repartidor.getTipo_vehiculo());

        try (Connection conn = sql2o.open()) {
            int usuarioId = (Integer) conn.createQuery(sql, true) // 'true' para permitir claves generadas
                    .addParameter("usuarioId", repartidor.getUsuario_id())
                    .addParameter("tipoVehiculo", repartidor.getTipo_vehiculo())
                    .executeUpdate()
                    .getKey(); // Obtiene el usuario_id generado

            repartidor.setUsuario_id(usuarioId); // Guarda el usuario_id en el objeto antes de retornarlo
            return repartidor;
        }
    }

    public void update(RepartidorEntity repartidor) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("UPDATE Repartidores SET tipo_vehiculo = :tipoVehiculo WHERE usuario_id = :usuarioId")
                    .addParameter("tipoVehiculo", repartidor.getTipo_vehiculo())
                    .addParameter("usuarioId", repartidor.getUsuario_id())
                    .executeUpdate();
        }
    }

    public void delete(Integer usuarioId) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DELETE FROM Repartidores WHERE usuario_id = :usuarioId")
                    .addParameter("usuarioId", usuarioId)
                    .executeUpdate();
        }
    }

    public List<Map<String, Object>> obtenerVistaDesempenoRepartidor() {
        String sql = "SELECT * FROM vista_desempeno_repartidor";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetchTable().asList();
        }
    }

    public List<Map<String, Object>> obtenerTopRepartidores() {
        String sql = """
    WITH Ranking AS (
        SELECT
            repartidor_id,
            ROUND(AVG(estrellas), 2) AS puntuacion,
            RANK() OVER (ORDER BY ROUND(AVG(estrellas), 2) DESC) AS ranking
        FROM calificaciones
        GROUP BY repartidor_id
        ORDER BY puntuacion DESC
    )
    SELECT
        I.nombre,
        I.apellido,
        R.puntuacion
    FROM Ranking R
    JOIN usuarios I ON R.repartidor_id = I.id
    WHERE R.ranking <= 3
    ORDER BY R.ranking;
    """;
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetchTable()
                    .asList();
        }
    }

    public List<RepartidorDistanciaMensualDTO> getDistanciaTotalRecorridaPorMes() {
        String sql = "SELECT " +
                "    U.Nombre AS Repartidor_Nombre, " +
                "    U.Apellido AS Repartidor_Apellido, " +
                "    TO_CHAR(P.fecha_entrega, 'YYYY-MM') AS Mes_Entrega, " +
                "    SUM(ST_Length(P.ruta_estimada::geography, true)) AS Distancia_Total_Metros " +
                "FROM " +
                "    Pedidos P " +
                "JOIN " +
                "    Usuarios U ON P.Repartidor_ID = U.ID " +
                "WHERE " +
                "    P.estado_entrega = 'Entregado' " +
                "    AND P.fecha_entrega IS NOT NULL " +
                "    AND P.ruta_estimada IS NOT NULL " +
                "GROUP BY " +
                "    U.ID, U.Nombre, U.Apellido, Mes_Entrega " +
                "ORDER BY " +
                "    Repartidor_Nombre, Repartidor_Apellido, Mes_Entrega DESC;";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(RepartidorDistanciaMensualDTO.class);
        }
    }

}