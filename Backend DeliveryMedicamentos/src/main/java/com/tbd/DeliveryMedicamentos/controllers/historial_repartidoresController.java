package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.entities.historial_repartidoresEntity;
import com.tbd.DeliveryMedicamentos.services.historial_repartidoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/historial_repartidores")
public class historial_repartidoresController {

    private final historial_repartidoresService service;

    @Autowired
    public historial_repartidoresController(historial_repartidoresService service) {
        this.service = service;
    }

    @PostMapping
    public historial_repartidoresEntity crear(@RequestBody historial_repartidoresEntity historial) {
        return service.crear(historial);
    }

    @GetMapping
    public List<historial_repartidoresEntity> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Optional<historial_repartidoresEntity> obtenerPorId(@PathVariable String id) {
        return service.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public historial_repartidoresEntity actualizar(@PathVariable String id, @RequestBody historial_repartidoresEntity actualizado) {
        return service.actualizar(id, actualizado);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        service.eliminarPorId(id);
    }
}
