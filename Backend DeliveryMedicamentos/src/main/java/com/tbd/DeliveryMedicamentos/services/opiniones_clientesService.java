package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.DTO.OpinionesPorHoraDTO;
import com.tbd.DeliveryMedicamentos.DTO.PromedioPuntuacionPorEmpresaDTO;
import com.tbd.DeliveryMedicamentos.entities.opiniones_clientesEntity;
import com.tbd.DeliveryMedicamentos.repositories.opiniones_clientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class opiniones_clientesService {

    private final opiniones_clientesRepository repository;

    @Autowired
    public opiniones_clientesService(opiniones_clientesRepository repository) {
        this.repository = repository;
    }

    // Crear una nueva opinión
    public opiniones_clientesEntity crearOpinion(opiniones_clientesEntity opinion) {
        opinion.setFecha(Instant.now()); // Establecer fecha actual
        return repository.save(opinion);
    }

    // Obtener todas las opiniones
    public List<opiniones_clientesEntity > obtenerTodas() {
        return repository.findAll();
    }

    // Buscar opiniones por palabras clave en el comentario
    public List<opiniones_clientesEntity> buscarPorPalabraClave(String palabraClave) {
        String regex = ".*" + palabraClave + ".*";  // patrón flexible
        return repository.buscarPorComentarioInsensitive(regex);
    }

    // Obtener opiniones por cliente
    public List<opiniones_clientesEntity > obtenerPorCliente(String clienteId) {
        return repository.findByClienteId(clienteId);
    }

    // Obtener opiniones por empresa
    public List<opiniones_clientesEntity > obtenerPorEmpresa(String empresaId) {
        return repository.findByEmpresaId(empresaId);
    }

    // Consulta 6. Agrupar opiniones por hora
    public List<OpinionesPorHoraDTO> analizarPatronesPorHora() {
        return repository.agruparOpinionesPorHora();
    }

    //Consulta 1: Obtener el promedio de puntuación por empresa
    public List<PromedioPuntuacionPorEmpresaDTO> obtenerPromedioPuntuacionPorEmpresa() {
        return repository.obtenerPromedioPuntuacionPorEmpresa();
    }
}