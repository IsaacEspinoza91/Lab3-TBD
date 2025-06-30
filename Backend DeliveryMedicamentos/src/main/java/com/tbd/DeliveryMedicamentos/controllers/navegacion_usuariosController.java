package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.DTO.ClienteSinCompraDTO;
import com.tbd.DeliveryMedicamentos.entities.navegacion_usuariosEntity;
import com.tbd.DeliveryMedicamentos.services.navegacion_usuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/navegacion_usuarios")
public class navegacion_usuariosController {

    private final navegacion_usuariosService service;

    @Autowired
    public navegacion_usuariosController(navegacion_usuariosService service) {
        this.service = service;
    }

    @PostMapping
    public navegacion_usuariosEntity crear(@RequestBody navegacion_usuariosEntity nav) {
        return service.crear(nav);
    }

    @GetMapping
    public List<navegacion_usuariosEntity> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Optional<navegacion_usuariosEntity> obtenerPorId(@PathVariable String id) {
        return service.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public navegacion_usuariosEntity actualizar(@PathVariable String id, @RequestBody navegacion_usuariosEntity actualizado) {
        return service.actualizar(id, actualizado);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        service.eliminarPorId(id);
    }

    //Consulta 5: Detectar clientes que realizaron búsquedas sin concretar pedidos (navegación sin compra).
    @GetMapping("/clientes-sin-compra")
    public ResponseEntity<List<ClienteSinCompraDTO>> getClientesSinCompra() {
        List<ClienteSinCompraDTO> clientes = service.getClientesSinCompraTrasBusqueda();
        return ResponseEntity.ok(clientes);
    }
}
