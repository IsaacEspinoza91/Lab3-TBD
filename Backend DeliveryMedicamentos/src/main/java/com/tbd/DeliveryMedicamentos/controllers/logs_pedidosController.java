package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.entities.logs_pedidosEntity;
import com.tbd.DeliveryMedicamentos.services.logs_pedidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/logs_pedidos")
public class logs_pedidosController {

    private final logs_pedidosService service;

    @Autowired
    public logs_pedidosController(logs_pedidosService service) {
        this.service = service;
    }

    @PostMapping
    public logs_pedidosEntity crearLog(@RequestBody logs_pedidosEntity log) {
        return service.crearLog(log);
    }

    @GetMapping
    public List<logs_pedidosEntity> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Optional<logs_pedidosEntity> obtenerPorId(@PathVariable String id) {
        return service.obtenerPorId(id);
    }

    @GetMapping("/pedido/{pedidoId}")
    public List<logs_pedidosEntity> obtenerPorPedidoId(@PathVariable String pedidoId) {
        return service.obtenerPorPedidoId(pedidoId);
    }

    @PutMapping("/{id}")
    public logs_pedidosEntity actualizar(@PathVariable String id, @RequestBody logs_pedidosEntity log) {
        return service.actualizarLog(id, log);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        service.eliminarPorId(id);
    }

    @GetMapping("/pedidos-cambiantes")
    public List<String> obtenerPedidosConMasDe3CambiosEn10Min() {
        return service.obtenerPedidosConMasDe3CambiosEn10Min();
    }
}
