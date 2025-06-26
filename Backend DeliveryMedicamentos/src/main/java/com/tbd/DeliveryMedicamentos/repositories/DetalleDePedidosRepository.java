package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.entities.DetalleDePedidosEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class DetalleDePedidosRepository {
    private final Sql2o sql2o;

    @Autowired
    public DetalleDePedidosRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<DetalleDePedidosEntity> findAll() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Detalle_de_pedidos").executeAndFetch(DetalleDePedidosEntity.class);
        }
    }

    public List<DetalleDePedidosEntity> findByPedidoId(int pedidoId) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Detalle_de_pedidos WHERE pedido_id = :pedidoId")
                    .addParameter("pedidoId", pedidoId)
                    .executeAndFetch(DetalleDePedidosEntity.class);
        }
    }

    public DetalleDePedidosEntity findById(Integer id) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Detalle_de_pedidos WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(DetalleDePedidosEntity.class);
        }
    }

    public DetalleDePedidosEntity save(DetalleDePedidosEntity detalle) {
        try (Connection conn = sql2o.open()) {
            int id = (Integer) conn.createQuery("INSERT INTO Detalle_de_pedidos(pedido_id, producto_id, cantidad) " +
                            "VALUES (:pedidoId, :productoId, :cantidad)", true)
                    .addParameter("pedidoId", detalle.getPedido_id())
                    .addParameter("productoId", detalle.getProducto_id())
                    .addParameter("cantidad", detalle.getCantidad())
                    .executeUpdate()
                    .getKey();
            detalle.setId(id);
            return detalle;
        }
    }

    public void update(DetalleDePedidosEntity detalle) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("UPDATE Detalle_de_pedidos SET pedido_id = :pedidoId, producto_id = :productoId, " +
                            "cantidad = :cantidad WHERE id = :id")
                    .addParameter("pedidoId", detalle.getPedido_id())
                    .addParameter("productoId", detalle.getProducto_id())
                    .addParameter("cantidad", detalle.getCantidad())
                    .addParameter("id", detalle.getId())
                    .executeUpdate();
        }
    }

    public void delete(Integer id) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DELETE FROM Detalle_de_pedidos WHERE id = :id")
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    public void insertarDetalleDePedido(DetalleDePedidosEntity detalle) {
        String sql = "INSERT INTO detalle_de_pedidos (pedido_id, producto_id, cantidad) VALUES (:pedidoId, :productoId, :cantidad)";


        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("pedidoId", detalle.getPedido_id())
                    .addParameter("productoId", detalle.getProducto_id())
                    .addParameter("cantidad", detalle.getCantidad())
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al insertar el detalle de pedido: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> productosMasPedidosPorCategoriaUltimoMes() {
        try (Connection conn = sql2o.open()) {
            String sql = """
            SELECT 
                p.nombre AS producto,
                p.requiere_receta AS requiere_receta,
                SUM(dp.cantidad) AS total_pedidos
            FROM 
                productos p
            JOIN 
                detalle_de_pedidos dp ON p.id = dp.producto_id
            JOIN 
                pedidos pe ON pe.id = dp.pedido_id
            WHERE 
                pe.fecha >= DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '1 month'
                AND pe.fecha < DATE_TRUNC('month', CURRENT_DATE)
            GROUP BY 
                p.id, p.nombre, p.requiere_receta
            ORDER BY 
                p.requiere_receta DESC, total_pedidos DESC;
        """;

            return conn.createQuery(sql)
                    .executeAndFetchTable()
                    .asList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }




}