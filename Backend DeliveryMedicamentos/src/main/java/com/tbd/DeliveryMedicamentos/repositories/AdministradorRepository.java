package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.entities.AdministradorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdministradorRepository {
    private final Sql2o sql2o;

    @Autowired
    public AdministradorRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<AdministradorEntity> findAll() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Administradores")
                    .executeAndFetch(AdministradorEntity.class);
        }
    }

    public AdministradorEntity findByUsuarioId(Integer usuarioId) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Administradores WHERE usuario_id = :usuarioId")
                    .addParameter("usuarioId", usuarioId)
                    .executeAndFetchFirst(AdministradorEntity.class);
        }
    }

    public AdministradorEntity save(AdministradorEntity administrador) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("INSERT INTO Administradores(usuario_id, nivel_acceso, departamento) " +
                            "VALUES (:usuarioId, :nivelAcceso, :departamento)")
                    .addParameter("usuarioId", administrador.getUsuario_id())
                    .addParameter("nivelAcceso", administrador.getNivel_acceso())
                    .addParameter("departamento", administrador.getDepartamento())
                    .executeUpdate();
            return administrador;
        }
    }

    public void update(AdministradorEntity administrador) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("UPDATE Administradores SET nivel_acceso = :nivelAcceso, " +
                            "departamento = :departamento WHERE usuario_id = :usuarioId")
                    .addParameter("nivelAcceso", administrador.getNivel_acceso())
                    .addParameter("departamento", administrador.getDepartamento())
                    .addParameter("usuarioId", administrador.getUsuario_id())
                    .executeUpdate();
        }
    }

    public void delete(Integer usuarioId) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DELETE FROM Administradores WHERE usuario_id = :usuarioId")
                    .addParameter("usuarioId", usuarioId)
                    .executeUpdate();
        }
    }
}