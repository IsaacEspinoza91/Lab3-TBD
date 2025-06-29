package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.entities.logs_pedidosEntity;
import com.tbd.DeliveryMedicamentos.repositories.logs_pedidosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class logs_pedidosService {

    private final logs_pedidosRepository repository;

    @Autowired
    public logs_pedidosService(logs_pedidosRepository repository) {
        this.repository = repository;
    }

    public logs_pedidosEntity crearLog(logs_pedidosEntity log) {
        return repository.save(log);
    }

    public List<logs_pedidosEntity> obtenerTodos() {
        return repository.findAll();
    }

    public Optional<logs_pedidosEntity> obtenerPorId(String id) {
        return repository.findById(id);
    }

    public void eliminarPorId(String id) {
        repository.deleteById(id);
    }

    public logs_pedidosEntity actualizarLog(String id, logs_pedidosEntity logActualizado) {
        if (repository.existsById(id)) {
            logActualizado.setPedido_id(id);
            return repository.save(logActualizado);
        }
        return null;
    }
}
