package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.DTO.ClienteDetalladoDTO;
import com.tbd.DeliveryMedicamentos.DTO.ResumenPedidoClienteDTO;
import com.tbd.DeliveryMedicamentos.DTO.ClienteTopGastoDTO;
import com.tbd.DeliveryMedicamentos.entities.ClienteEntity;
import com.tbd.DeliveryMedicamentos.entities.UsuarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ClienteRepository {
    private final Sql2o sql2o;

    @Autowired
    public ClienteRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<ClienteEntity> findAll() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Clientes").executeAndFetch(ClienteEntity.class);
        }
    }

    public ClienteEntity findByUsuarioId(Integer usuarioId) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Clientes WHERE usuario_id = :usuarioId")
                    .addParameter("usuarioId", usuarioId)
                    .executeAndFetchFirst(ClienteEntity.class);
        }
    }

    public ClienteEntity save(ClienteEntity cliente) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("INSERT INTO Clientes(usuario_id, direccion) VALUES (:usuarioId, :direccion)")
                    .addParameter("usuarioId", cliente.getUsuario_id())
                    .addParameter("direccion", cliente.getDireccion())
                    .executeUpdate();
            return cliente;
        }
    }

    public void update(ClienteEntity cliente) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("UPDATE Clientes SET direccion = :direccion WHERE usuario_id = :usuarioId")
                    .addParameter("direccion", cliente.getDireccion())
                    .addParameter("usuarioId", cliente.getUsuario_id())
                    .executeUpdate();
        }
    }

    public void delete(Integer usuarioId) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DELETE FROM Clientes WHERE usuario_id = :usuarioId")
                    .addParameter("usuarioId", usuarioId)
                    .executeUpdate();
        }
    }

    public List<ClienteDetalladoDTO> obtenerClientesDetallados() {
        String sql = """
        SELECT
            c.usuario_id AS usuarioId,
            u.nombre,
            u.email,
            c.direccion
        FROM clientes c
        JOIN usuarios u ON c.usuario_id = u.id
    """;

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .executeAndFetch(ClienteDetalladoDTO.class);
        }
    }

    public Map<String, Object> obtenerClienteQueMasGasto() {
        String sql = """
        SELECT u.Nombre, u.Apellido, c.Usuario_ID AS Cliente_ID, 
               SUM(p.Total_pagado) AS TotalGastado
        FROM Clientes c
        JOIN Usuarios u ON c.Usuario_ID = u.ID
        JOIN Pedidos p ON p.Cliente_ID = c.Usuario_ID
        WHERE p.Estado_entrega = 'Entregado'
        GROUP BY c.Usuario_ID, u.Nombre, u.Apellido
        ORDER BY TotalGastado DESC
        LIMIT 1;
    """;

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetchTable().asList().stream()
                    .findFirst().orElse(null);
        }
    }

    public List<ResumenPedidoClienteDTO> getResumenPedidos() {
        String sql = "SELECT * FROM resumen_pedidos_por_cliente";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(ResumenPedidoClienteDTO.class);
        }
    }

    public ClienteTopGastoDTO findClienteMayorGasto() {
        try (Connection connection = sql2o.open()) {
            String sql = "SELECT u.nombre AS nombre, u.apellido AS apellido, " +
                    "c.usuario_id AS clienteId, SUM(p.total_pagado) AS totalGastado " +
                    "FROM clientes c " +
                    "JOIN usuarios u ON c.usuario_id = u.id " +
                    "JOIN pedidos p ON p.cliente_id = c.usuario_id " +
                    "WHERE p.estado_entrega = 'Entregado' " +
                    "GROUP BY c.usuario_id, u.nombre, u.apellido " +
                    "ORDER BY totalGastado DESC " +
                    "LIMIT 1";

            return connection.createQuery(sql)
                    .executeAndFetchFirst(ClienteTopGastoDTO.class);
        }
    }

    public List<ClienteDetalladoDTO> findClientesLejanosA5kmDeFarmacia() {
        String sql = """
        SELECT
            u.id AS usuarioId, -- Cambiado de 'id' a 'usuarioId'
            u.nombre AS nombre,
            u.email AS email
            -- Si 'direccion' existe en 'usuarios', agrégala aquí:
            -- u.direccion AS direccion
        FROM
            usuarios u
        WHERE
            u.tipo = 'CLIENTE'
            AND u.geom IS NOT NULL
            AND NOT EXISTS (
                SELECT 1
                FROM farmacias f
                WHERE ST_DWithin(
                    ST_SetSRID(u.geom, 4326)::geography,
                    ST_SetSRID(f.geom, 4326)::geography,
                    5000
                )
            );
        """;

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .executeAndFetch(ClienteDetalladoDTO.class);
        }
    }
}