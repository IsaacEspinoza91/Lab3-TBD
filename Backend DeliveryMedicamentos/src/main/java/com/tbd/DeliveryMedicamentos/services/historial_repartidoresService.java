package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.DTO.RutaSecuenciaDTO;
import com.tbd.DeliveryMedicamentos.entities.RutaEntity;
import com.tbd.DeliveryMedicamentos.entities.historial_repartidoresEntity;
import com.tbd.DeliveryMedicamentos.repositories.historial_repartidoresRepository;
import com.tbd.DeliveryMedicamentos.DTO.RutaFrecuenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<RutaSecuenciaDTO> obtenerRutasCompletasFrecuentesUltimos7Dias() {
        List<historial_repartidoresEntity> todos = repository.findAll();
        Instant hace7dias = Instant.now().minus(7, ChronoUnit.DAYS);

        Map<String, Integer> contadorRutas = new HashMap<>();

        for (historial_repartidoresEntity hr : todos) {
            // Filtrar puntos recientes
            List<RutaEntity> recientes = hr.getRutas().stream()
                    .filter(r -> {
                        try {
                            Instant tiempo = Instant.parse(r.getTimestamp());
                            return tiempo.isAfter(hace7dias);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());

            if (recientes.isEmpty()) continue;

            // Codificar la secuencia de puntos como string
            String rutaCodificada = recientes.stream()
                    .map(r -> String.format("%.4f,%.4f", r.getLat(), r.getLng()))
                    .collect(Collectors.joining("|"));

            contadorRutas.put(rutaCodificada, contadorRutas.getOrDefault(rutaCodificada, 0) + 1);
        }

        // Convertir a lista de DTO y ordenar por frecuencia
        return contadorRutas.entrySet().stream()
                .map(e -> new RutaSecuenciaDTO(e.getKey(), e.getValue()))
                .sorted((a, b) -> Integer.compare(b.getFrecuencia(), a.getFrecuencia()))
                .limit(10)
                .collect(Collectors.toList());
    }
}
