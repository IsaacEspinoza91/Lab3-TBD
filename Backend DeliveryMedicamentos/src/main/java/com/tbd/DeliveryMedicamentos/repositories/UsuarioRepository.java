package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.DTO.ZonaCoberturaDTO;
import com.tbd.DeliveryMedicamentos.DTO.ZonaUsuarioDTO;
import com.tbd.DeliveryMedicamentos.entities.UsuarioEntity;
import com.tbd.DeliveryMedicamentos.entities.ZonasEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepository {
    private final Sql2o sql2o;

    @Autowired
    public UsuarioRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<UsuarioEntity> findAll() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Usuarios").executeAndFetch(UsuarioEntity.class);
        }
    }

    public ZonaUsuarioDTO obtenerZonaDeCliente(int idCliente) {
        String sql = """
        SELECT 
            u.Nombre AS nombreCliente,
            z.nombre AS nombreZona,
            ST_AsGeoJSON(u.geom) AS ubicacionCliente,
            ST_AsGeoJSON(z.geom) AS poligonoZona
        FROM Usuarios u
        JOIN Zonas_cobertura z ON ST_Within(u.geom, z.geom)
        WHERE u.ID = :idCliente AND u.Tipo = 'CLIENTE'
        LIMIT 1
    """;

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("idCliente", idCliente)
                    .executeAndFetchFirst(ZonaUsuarioDTO.class);
        }
    }

    public ZonaUsuarioDTO verZonaUser(int idCliente) {
        String sql = """
        SELECT 
            nombre_cliente AS nombreCliente,
            nombre_zona AS nombreZona,
            ubicacion_cliente AS ubicacionCliente,
            poligono_zona AS poligonoZona
        FROM Zonas_clientes
        WHERE cliente_id = :idCliente
        LIMIT 1
    """;

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("idCliente", idCliente)
                    .executeAndFetchFirst(ZonaUsuarioDTO.class);
        }
    }



    public UsuarioEntity findById(Integer id) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Usuarios WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(UsuarioEntity.class);
        }
    }

    public UsuarioEntity findByEmail(String email) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Usuarios WHERE email = :email")
                    .addParameter("email", email)
                    .executeAndFetchFirst(UsuarioEntity.class);
        }
    }

    public UsuarioEntity save(UsuarioEntity usuario) {
        try (Connection conn = sql2o.open()) {
            int id = (Integer) conn.createQuery("INSERT INTO Usuarios(rut, nombre, apellido, email, password, telefono, tipo, geom) " +
                            "VALUES (:rut, :nombre, :apellido, :email, :password, :telefono, :tipo, ST_GeomFromText(:geom, 4326))", true)
                    .addParameter("rut", usuario.getRut())
                    .addParameter("nombre", usuario.getNombre())
                    .addParameter("apellido", usuario.getApellido())
                    .addParameter("email", usuario.getEmail())
                    .addParameter("password", usuario.getPassword())
                    .addParameter("telefono", usuario.getTelefono())
                    .addParameter("tipo", usuario.getTipo())
                    .addParameter("geom", usuario.getGeom())
                    .executeUpdate()
                    .getKey();
            usuario.setId(id);
            return usuario;
        }
    }

    /*Formato
    POST http://localhost:8080/api/auth/register
    {
    "rut": "91839492-2",
    "nombre": "Pose",
    "apellido": "Pérez",
    "email": "Pu4nKi@example.com",
    "telefono": "+56934542311",
    "password": "Usachito2025TDB",
    "tipo": "CLIENTE",
    "lat": -33.734539,
    "lng": -70.860034
    }
     */

    public void update(UsuarioEntity usuario) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("UPDATE Usuarios SET rut = :rut, nombre = :nombre, apellido = :apellido, " +
                            "email = :email, password = :password, telefono = :telefono, tipo = :tipo " +
                            "WHERE id = :id")
                    .addParameter("rut", usuario.getRut())
                    .addParameter("nombre", usuario.getNombre())
                    .addParameter("apellido", usuario.getApellido())
                    .addParameter("email", usuario.getEmail())
                    .addParameter("password", usuario.getPassword())
                    .addParameter("telefono", usuario.getTelefono())
                    .addParameter("tipo", usuario.getTipo())
                    .addParameter("id", usuario.getId())
                    .executeUpdate();
        }
    }

    public void delete(Integer id) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DELETE FROM Usuarios WHERE id = :id")
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    public long count() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT COUNT(*) FROM Usuarios")
                    .executeScalar(Long.class);
        }
    }

    public List<ZonaCoberturaDTO> obtenerZonasPorUsuario(int usuarioId) {
        String sql = """
        SELECT u.id as idUsuario,
               z.id as idZonaCobertura, 
               z.nombre as nombreZonaCobertura
        FROM zonas_cobertura z
        JOIN usuarios u ON u.id = :usuarioId
        WHERE ST_Intersects(z.geom, u.geom)
        """;

        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("usuarioId", usuarioId)
                    .executeAndFetch(ZonaCoberturaDTO.class);
        }
    }
}