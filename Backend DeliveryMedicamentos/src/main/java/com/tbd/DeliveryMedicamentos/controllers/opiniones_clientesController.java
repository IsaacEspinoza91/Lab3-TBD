package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.DTO.OpinionesPorHoraDTO;
import com.tbd.DeliveryMedicamentos.entities.opiniones_clientesEntity;
import com.tbd.DeliveryMedicamentos.services.opiniones_clientesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opiniones_clientes")
public class opiniones_clientesController {
    private final opiniones_clientesService service;

    @Autowired
    public opiniones_clientesController(opiniones_clientesService service) {
        this.service = service;
    }

    @PostMapping
    public opiniones_clientesEntity  crearOpinion(@RequestBody opiniones_clientesEntity opinion) {
        System.out.println(opinion);
        return service.crearOpinion(opinion);
    }

    @GetMapping
    public List<opiniones_clientesEntity > obtenerTodas() {
        System.out.println(service.obtenerTodas());
        return service.obtenerTodas();
    }

    // Endpoint para buscar opiniones por palabras clave
    @GetMapping("/buscar/{keyword}")
    public List<opiniones_clientesEntity> buscarPorPalabraClave(@PathVariable String keyword) {
        return service.buscarPorPalabraClave(keyword);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<opiniones_clientesEntity > obtenerPorCliente(@PathVariable String clienteId) {
        return service.obtenerPorCliente(clienteId);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<opiniones_clientesEntity > obtenerPorEmpresa(@PathVariable String empresaId) {
        return service.obtenerPorEmpresa(empresaId);
    }

    @GetMapping("/por_hora")
    public List<OpinionesPorHoraDTO> getOpinionesPorHora() {
        return service.analizarPatronesPorHora();
    }
}
