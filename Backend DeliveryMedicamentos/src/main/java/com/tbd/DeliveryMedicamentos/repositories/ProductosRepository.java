package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.DTO.RankingProductosCanceladosDTO;
import com.tbd.DeliveryMedicamentos.DTO.RankingProductosDevueltosDTO;
import com.tbd.DeliveryMedicamentos.entities.ProductosEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductosRepository {
    private final Sql2o sql2o;

    @Autowired
    public ProductosRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<ProductosEntity> findAll() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Productos").executeAndFetch(ProductosEntity.class);
        }
    }

    public ProductosEntity findById(Integer id) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Productos WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(ProductosEntity.class);
        }
    }

    public ProductosEntity save(ProductosEntity producto) {
        try (Connection conn = sql2o.open()) {
            int id = (Integer) conn.createQuery("INSERT INTO Productos(nombre, precio, stock, requiere_receta) " +
                            "VALUES (:nombre, :precio, :stock, :requiereReceta)", true)
                    .addParameter("nombre", producto.getNombre())
                    .addParameter("precio", producto.getPrecio())
                    .addParameter("stock", producto.getStock())
                    .addParameter("requiereReceta", producto.getRequiere_receta())
                    .executeUpdate()
                    .getKey();
            producto.setId(id);
            return producto;
        }
    }

    public void update(ProductosEntity producto) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("UPDATE Productos SET nombre = :nombre, precio = :precio, " +
                            "stock = :stock, requiere_receta = :requiereReceta WHERE id = :id")
                    .addParameter("nombre", producto.getNombre())
                    .addParameter("precio", producto.getPrecio())
                    .addParameter("stock", producto.getStock())
                    .addParameter("requiereReceta", producto.getRequiere_receta())
                    .addParameter("id", producto.getId())
                    .executeUpdate();
        }
    }

    public void delete(Integer id) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DELETE FROM Productos WHERE id = :id")
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    public long count() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT COUNT(*) FROM Productos")
                    .executeScalar(Long.class);
        }
    }

    public List<RankingProductosCanceladosDTO> findProductosMasCancelados() {
        String sql = "SELECT " +
                "p.ID AS producto_id, " +
                "p.Nombre AS nombre_producto, " +
                "COUNT(dp.ID) AS veces_cancelado, " +
                "CONCAT( " +
                "    ROUND( " +
                "        COUNT(dp.ID) * 100.0 / " +
                "        NULLIF(SUM(COUNT(dp.ID)) OVER (), 0), " +
                "        2 " +
                "    ), " +
                "    '%' " +
                ") AS porcentaje_cancelaciones " +
                "FROM " +
                "    Productos p " +
                "JOIN " +
                "    Detalle_de_pedidos dp ON p.ID = dp.Producto_ID " +
                "JOIN " +
                "    Pedidos ped ON dp.Pedido_ID = ped.ID " +
                "WHERE " +
                "    ped.Estado_entrega = 'Cancelado' " +
                "GROUP BY " +
                "    p.ID, p.Nombre " +
                "ORDER BY " +
                "    veces_cancelado DESC " +
                "LIMIT 10";

        try (Connection connection = sql2o.open()) {
            return connection.createQuery(sql)
                    .executeAndFetch(RankingProductosCanceladosDTO.class);
        }
    }


    public List<RankingProductosDevueltosDTO> findProductosMasDevueltos() {
        String sql = "SELECT " +
                "p.ID AS producto_id, " +
                "p.Nombre AS nombre_producto, " +
                "COUNT(dp.ID) AS veces_devuelto, " +
                "CONCAT( " +
                "    ROUND( " +
                "        COUNT(dp.ID) * 100.0 / " +
                "        NULLIF(SUM(COUNT(dp.ID)) OVER (), 0), " +
                "        2 " +
                "    ), " +
                "    '%' " +
                ") AS porcentaje_devoluciones " +
                "FROM Productos p " +
                "JOIN Detalle_de_pedidos dp ON p.ID = dp.Producto_ID " +
                "JOIN Pedidos ped ON dp.Pedido_ID = ped.ID " +
                "WHERE ped.Estado_entrega = 'Devuelto' " +
                "GROUP BY p.ID, p.Nombre " +
                "ORDER BY veces_devuelto DESC " +
                "LIMIT 10";

        try (Connection connection = sql2o.open()) {
            return connection.createQuery(sql)
                    .executeAndFetch(RankingProductosDevueltosDTO.class);
        }
    }
}