package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.entities.historial_repartidoresEntity;
import com.tbd.DeliveryMedicamentos.repositories.historial_repartidoresRepository;
import com.tbd.DeliveryMedicamentos.DTO.RutaFrecuenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class historial_repartidoresService {

    private final historial_repartidoresRepository repository;

    @Autowired
    public historial_repartidoresService(historial_repartidoresRepository repository) {
        this.repository = repository;
    }

    public historial_repartidoresEntity crear(historial_repartidoresEntity historial) {
        return repository.save(historial);
    }

    public List<historial_repartidoresEntity> obtenerTodos() {
        return repository.findAll();
    }

    public Optional<historial_repartidoresEntity> obtenerPorId(String id) {
        return repository.findById(id);
    }

    public historial_repartidoresEntity actualizar(String id, historial_repartidoresEntity actualizado) {
        if (repository.existsById(id)) {
            actualizado.setRepartidor_id(id);
            return repository.save(actualizado);
        }
        return null;
    }

    public void eliminarPorId(String id) {
        repository.deleteById(id);
    }

    public List<RutaFrecuenteDTO> obtenerRutasFrecuentesUltimos7Dias() {
        Instant haceSieteDias = Instant.now().minus(7, ChronoUnit.DAYS);
        Date fechaInicio = Date.from(haceSieteDias);
        return repository.obtenerRutasFrecuentesDesde(fechaInicio);
    }
}
