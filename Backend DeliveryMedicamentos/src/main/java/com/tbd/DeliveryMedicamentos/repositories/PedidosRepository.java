package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.DTO.DetallePedidoDTO;
import com.tbd.DeliveryMedicamentos.DTO.ResumenPedidoClienteDTO;
import com.tbd.DeliveryMedicamentos.DTO.RutasZonasCruzadasDTO;
import com.tbd.DeliveryMedicamentos.entities.PedidosEntity;
import com.tbd.DeliveryMedicamentos.utils.ConverJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PedidosRepository {
    private final Sql2o sql2o;

    @Autowired
    public PedidosRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<PedidosEntity> findAll() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Pedidos").executeAndFetch(PedidosEntity.class);
        }
    }

    public List<RutasZonasCruzadasDTO> rutasZonasCruzadas() {
        String sql = """
        SELECT 
            p.id AS pedido_id,
            STRING_AGG(DISTINCT z.nombre, ', ') AS zonas_cruzadas,
            COUNT(DISTINCT z.id) AS cantidad_zonas
        FROM pedidos p
        JOIN zonas_cobertura z ON ST_Intersects(p.ruta_estimada, z.geom)
        GROUP BY p.id
        HAVING COUNT(DISTINCT z.id) > 2
        ORDER BY cantidad_zonas DESC
        """;

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addColumnMapping("pedido_id", "pedidoId")
                    .addColumnMapping("zonas_cruzadas", "zonasCruzadas")
                    .addColumnMapping("cantidad_zonas", "cantidadZonas")
                    .executeAndFetch(RutasZonasCruzadasDTO.class);
        }
    }


    public String mostrarRuta(int pedidoId) {
        String sql = "SELECT ST_AsGeoJSON(ruta_estimada) FROM Pedidos WHERE ID = :pedidoId";

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("pedidoId", pedidoId)
                    .executeScalar(String.class);
        }
    }

    public String mostrarRutaMultiLineString(int pedidoId) {
        String sql = "SELECT ST_AsGeoJSON(ruta_estimada_mls) FROM Pedidos WHERE ID = :pedidoId";

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("pedidoId", pedidoId)
                    .executeScalar(String.class);
        }
    }

    /*FORMATO
    GET http://localhost:8080/api/pedidos/{id}/ruta
     */

    public PedidosEntity findById(Integer id) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Pedidos WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(PedidosEntity.class);
        }
    }

    public PedidosEntity save(PedidosEntity pedido) {
        try (Connection conn = sql2o.open()) {
            System.out.println("Ruta antes de guardar: " + pedido.getRutaEstimada()); // Debugging

            int id = (Integer) conn.createQuery(
                            "INSERT INTO Pedidos(fecha, urgencia, total_pagado, estado_entrega, fecha_entrega, cliente_id, medio_pago_id, farmacia_id, repartidor_id, ruta_estimada) " +
                                    "VALUES (:fecha, :urgencia, :total_pagado, :estado_entrega, :fecha_entrega, :cliente_id, :medio_pago_id, :farmacia_id, :repartidor_id, ST_GeomFromText(:ruta_estimada, 4326))", true)
                    .addParameter("fecha", pedido.getFecha())
                    .addParameter("urgencia", pedido.getUrgencia())
                    .addParameter("total_pagado", pedido.getTotal_pagado())
                    .addParameter("estado_entrega", pedido.getEstado_entrega())
                    .addParameter("fecha_entrega", pedido.getFecha_entrega())
                    .addParameter("cliente_id", pedido.getCliente_id())
                    .addParameter("medio_pago_id", pedido.getMedio_pago_id())
                    .addParameter("farmacia_id", pedido.getFarmacia_id())
                    .addParameter("repartidor_id", pedido.getRepartidor_id())
                    .addParameter("ruta_estimada", pedido.getRutaEstimada())
                    .executeUpdate()
                    .getKey();

            pedido.setId(id);
            return pedido;
        }
    }

    /* Formato de consulta JSON
    POST http://localhost:8080/api/pedidos
    {
    "fecha": "2025-06-10",
    "urgencia": true,
    "total_pagado": 20000,
    "estado_entrega": "En camino",
    "fecha_entrega": "2025-06-11",
    "cliente_id": 1,
    "medio_pago_id": 2,
    "farmacia_id": 3,
    "repartidor_id": 4,
    "coordenadas": [
        [-33.516000, -70.760000],
        [-33.530000, -70.780000],
        [-33.540000, -70.800000],
        [-33.550000, -70.820000],
        [-33.560000, -70.840000]
    ]
    }

     */

    public void registrarPedido(PedidosEntity pedido, List<DetallePedidoDTO> detalles) {
        String sql = "CALL registrar_pedido(" +
                "CAST(:fecha AS date), " +
                "CAST(:urgencia AS boolean), " +
                "CAST(:total_pagado AS integer), " +
                "CAST(:estado_entrega AS varchar), " +
                "CAST(:fecha_entrega AS date), " +
                "CAST(:medio_pago_id AS integer), " +
                "CAST(:farmacia_id AS integer), " +
                "CAST(:repartidor_id AS integer), " +
                "CAST(:cliente_id AS integer), " +
                "CAST(:detalles AS json))";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("fecha", pedido.getFecha())
                    .addParameter("urgencia", pedido.getUrgencia())
                    .addParameter("total_pagado", pedido.getTotal_pagado())
                    .addParameter("estado_entrega", pedido.getEstado_entrega())
                    .addParameter("fecha_entrega", pedido.getFecha_entrega())
                    .addParameter("medio_pago_id", pedido.getMedio_pago_id())
                    .addParameter("farmacia_id", pedido.getFarmacia_id())
                    .addParameter("repartidor_id", pedido.getRepartidor_id())
                    .addParameter("cliente_id", pedido.getCliente_id())
                    .addParameter("detalles", ConverJsonUtil.toJson(detalles))
                    .executeUpdate();
        }
    }

    public Map<String, Object> obtenerMedioPagoMasUsadoEnUrgentes() {
        String sql = """
        SELECT mdp.tipo AS medio_pago, COUNT(*) AS cantidad_usos
        FROM pedidos p
        JOIN medios_de_pago mdp ON p.medio_pago_id = mdp.id
        WHERE p.urgencia = true
        GROUP BY mdp.tipo
        ORDER BY cantidad_usos DESC
        LIMIT 1;
    """;
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetchTable()
                    .asList()
                    .stream()
                    .findFirst()
                    .orElse(null);
        }
    }


    public List<ResumenPedidoClienteDTO> obtenerResumenPedidos() {
        String sql = "SELECT * FROM resumen_pedidos_por_cliente";

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .executeAndFetch(ResumenPedidoClienteDTO.class);
        }
    }

    public void cambiarEstadoPedido(int idPedido, String nuevoEstado) {
        String sql = "CALL cambiar_estado_pedido(:idPedido, :nuevoEstado)";

        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("idPedido", idPedido)
                    .addParameter("nuevoEstado", nuevoEstado)
                    .executeUpdate();
        } catch (Exception e) {
            // El mensaje de error vendrá de las RAISE EXCEPTION
            throw new RuntimeException(e.getMessage());
        }
    }

    public void update(PedidosEntity pedido) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("UPDATE Pedidos SET fecha = :fecha, urgencia = :urgencia, total_pagado = :totalPagado, " +
                            "estado_entrega = :estadoEntrega, fecha_entrega = :fechaEntrega, cliente_id = :clienteId, " +
                            "medio_pago_id = :medioPagoId, farmacia_id = :farmaciaId, repartidor_id = :repartidorId " +
                            "WHERE id = :id")
                    .addParameter("fecha", pedido.getFecha())
                    .addParameter("urgencia", pedido.getUrgencia())
                    .addParameter("totalPagado", pedido.getTotal_pagado())
                    .addParameter("estadoEntrega", pedido.getEstado_entrega())
                    .addParameter("fechaEntrega", pedido.getFecha_entrega())
                    .addParameter("clienteId", pedido.getCliente_id())
                    .addParameter("medioPagoId", pedido.getMedio_pago_id())
                    .addParameter("farmaciaId", pedido.getFarmacia_id())
                    .addParameter("repartidorId", pedido.getRepartidor_id())
                    .addParameter("id", pedido.getId())
                    .executeUpdate();
        }
    }

    public void delete(Integer id) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DELETE FROM Pedidos WHERE id = :id")
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    public List<Map<String, Object>> tiempoPromedioPorRepartidor() {
        try (Connection conn = sql2o.open()) {
            String sql = """
        SELECT 
            u.nombre AS repartidores_nombre,
            ROUND(AVG(p.fecha_entrega - p.fecha), 1) AS tiempo_promedio_dias
        FROM Pedidos p
        JOIN Repartidores r ON p.repartidor_id = r.usuario_id
        JOIN Usuarios u ON r.usuario_id = u.id
        WHERE p.fecha_entrega IS NOT NULL
        GROUP BY u.id, u.nombre;
        """;
            return conn.createQuery(sql)
                    .executeAndFetchTable()
                    .asList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> tiempoPromedioPorRepartidorId(int repartidorId) {
        try (Connection conn = sql2o.open()) {
            String sql = """
        SELECT 
            u.nombre AS repartidores_nombre,
            ROUND(AVG(p.fecha_entrega - p.fecha), 1) AS tiempo_promedio_dias
        FROM Pedidos p
        JOIN Repartidores r ON p.repartidor_id = r.usuario_id
        JOIN Usuarios u ON r.usuario_id = u.id
        WHERE p.fecha_entrega IS NOT NULL
        AND r.usuario_id = :repartidorId
        GROUP BY u.id, u.nombre;
        """;
            return conn.createQuery(sql)
                    .addParameter("repartidorId", repartidorId)
                    .executeAndFetchTable()
                    .asList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Map<String, Object> medioPagoMasUsadoEnUrgencias() {
        try (Connection conn = sql2o.open()) {
            String sql = """
            SELECT mdp.tipo AS medio_pago, COUNT(*) AS cantidad_usos
            FROM pedidos p
            JOIN medios_de_pago mdp ON p.medio_pago_id = mdp.id
            WHERE p.urgencia = true
            GROUP BY mdp.tipo
            ORDER BY cantidad_usos DESC
            LIMIT 1;
        """;

            List<Map<String, Object>> result = conn.createQuery(sql)
                    .executeAndFetchTable()
                    .asList();

            return result.isEmpty() ? null : result.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public long countPedidosMesActual() {
        try (Connection connection = sql2o.open()) {
            String sql = "SELECT COUNT(*) FROM pedidos " +
                    "WHERE EXTRACT(MONTH FROM fecha) = EXTRACT(MONTH FROM CURRENT_DATE) " +
                    "AND EXTRACT(YEAR FROM fecha) = EXTRACT(YEAR FROM CURRENT_DATE)";

            return connection.createQuery(sql)
                    .executeScalar(Long.class);
        }
    }

    public List<Map<String, Object>> farmaciasConMasEntregasFallidas() {
        try (Connection conn = sql2o.open()) {
            String sql = """
            SELECT f.id, f.nombre, f.lugar, COUNT(*) AS entregas_fallidas
            FROM pedidos p
            INNER JOIN farmacias f ON p.farmacia_id = f.id
            WHERE p.estado_entrega = 'Fallido'
            GROUP BY f.id, f.nombre, f.lugar
            ORDER BY entregas_fallidas DESC
        """;
            return conn.createQuery(sql)
                    .executeAndFetchTable()
                    .asList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Map<String, Object> clienteConMasGastoEnPedidosEntregados() {
        try (Connection conn = sql2o.open()) {
            String sql = """
            SELECT u.Nombre, u.Apellido, c.Usuario_ID AS Cliente_ID, SUM(p.Total_pagado) AS TotalGastado
            FROM Clientes c
            JOIN Usuarios u ON c.Usuario_ID = u.ID
            JOIN Pedidos p ON p.Cliente_ID = c.Usuario_ID
            WHERE p.Estado_entrega = 'Entregado'
            GROUP BY c.Usuario_ID, u.Nombre, u.Apellido
            ORDER BY TotalGastado DESC
            LIMIT 1;
        """;
            List<Map<String, Object>> result = conn.createQuery(sql)
                    .executeAndFetchTable()
                    .asList();

            return result.isEmpty() ? null : result.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Integer> obtenerPedidosConNotificaciones() {
        try (Connection conn = sql2o.open()) {
            String sql = """
            SELECT DISTINCT pedido_id
            FROM Notificaciones;
        """;
            return conn.createQuery(sql)
                    .executeAndFetch(Integer.class);
        }
    }

    public boolean actualizarEstadoEntrega(int pedidoId, String nuevoEstado) {
        try (Connection conn = sql2o.open()) {
            String sql = "UPDATE Pedidos SET estado_entrega = :estadoEntrega WHERE id = :id";
            int rowsAffected = conn.createQuery(sql)
                    .addParameter("estadoEntrega", nuevoEstado)
                    .addParameter("id", pedidoId)
                    .executeUpdate()
                    .getResult();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Integer> obtenerPedidosTardados() {
        try (Connection conn = sql2o.open()) {
            String sql = """
                SELECT id
                FROM Pedidos
                WHERE estado_entrega = 'Tardado'
                """;
            return conn.createQuery(sql)
                    .executeAndFetch(Integer.class);
        }
    }

    public void descontarStockConfirmado(int pedidoId) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("CALL descontar_stock_confirmado(:pedidoId)")
                    .addParameter("pedidoId", pedidoId)
                    .executeUpdate();
        }
    }
}