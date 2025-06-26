package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.entities.PedidosRepartidoresEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

@Repository
public class PedidosRepartidoresRepository {
    private final Sql2o sql2o;

    @Autowired
    public PedidosRepartidoresRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<PedidosRepartidoresEntity> findAll() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Pedidos_Repartidores")
                    .executeAndFetch(PedidosRepartidoresEntity.class);
        }
    }

    public List<PedidosRepartidoresEntity> findByPedidoId(int pedidoId) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Pedidos_Repartidores WHERE pedido_id = :pedidoId")
                    .addParameter("pedidoId", pedidoId)
                    .executeAndFetch(PedidosRepartidoresEntity.class);
        }
    }

    public List<PedidosRepartidoresEntity> findByRepartidorId(int repartidorId) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Pedidos_Repartidores WHERE repartidor_id = :repartidorId")
                    .addParameter("repartidorId", repartidorId)
                    .executeAndFetch(PedidosRepartidoresEntity.class);
        }
    }

    public void save(PedidosRepartidoresEntity pedidoRepartidor) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("INSERT INTO Pedidos_Repartidores (pedido_id, repartidor_id) " +
                            "VALUES (:pedidoId, :repartidorId)")
                    .addParameter("pedidoId", pedidoRepartidor.getPedidoId())
                    .addParameter("repartidorId", pedidoRepartidor.getRepartidor_id())
                    .executeUpdate();
        }
    }

    public void delete(PedidosRepartidoresEntity pedidoRepartidor) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DELETE FROM Pedidos_Repartidores WHERE pedido_id = :pedidoId AND repartidor_id = :repartidorId")
                    .addParameter("pedidoId", pedidoRepartidor.getPedidoId())
                    .addParameter("repartidorId", pedidoRepartidor.getRepartidor_id())
                    .executeUpdate();
        }
    }
}
