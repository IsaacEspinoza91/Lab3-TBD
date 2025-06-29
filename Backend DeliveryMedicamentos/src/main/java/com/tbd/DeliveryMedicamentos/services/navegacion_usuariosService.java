package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.entities.navegacion_usuariosEntity;
import com.tbd.DeliveryMedicamentos.repositories.navegacion_usuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class navegacion_usuariosService {

    private final navegacion_usuariosRepository repository;

    @Autowired
    public navegacion_usuariosService(navegacion_usuariosRepository repository) {
        this.repository = repository;
    }

    public navegacion_usuariosEntity crear(navegacion_usuariosEntity nav) {
        return repository.save(nav);
    }

    public List<navegacion_usuariosEntity> obtenerTodos() {
        return repository.findAll();
    }

    public Optional<navegacion_usuariosEntity> obtenerPorId(String id) {
        return repository.findById(id);
    }

    public navegacion_usuariosEntity actualizar(String id, navegacion_usuariosEntity actualizado) {
        if (repository.existsById(id)) {
            actualizado.setCliente_id(id);
            return repository.save(actualizado);
        }
        return null;
    }

    public void eliminarPorId(String id) {
        repository.deleteById(id);
    }
}
