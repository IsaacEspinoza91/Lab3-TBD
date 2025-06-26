package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.entities.DetalleDePedidosEntity;
import com.tbd.DeliveryMedicamentos.services.DetalleDePedidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/detalle_pedidos")
public class DetalleDePedidosController {
    private final DetalleDePedidosService detallePedidoService;

    @Autowired
    public DetalleDePedidosController(DetalleDePedidosService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping
    public ResponseEntity<List<DetalleDePedidosEntity>> getAllDetallesPedido() {
        List<DetalleDePedidosEntity> detalles = detallePedidoService.getAllDetallesPedido();
        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<DetalleDePedidosEntity>> getDetallesByPedidoId(@PathVariable int pedidoId) {
        List<DetalleDePedidosEntity> detalles = detallePedidoService.getDetallesByPedidoId(pedidoId);
        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleDePedidosEntity> getDetallePedidoById(@PathVariable int id) {
        DetalleDePedidosEntity detalle = detallePedidoService.getDetallePedidoById(id);
        return new ResponseEntity<>(detalle, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DetalleDePedidosEntity> createDetallePedido(@RequestBody DetalleDePedidosEntity detallePedido) {
        DetalleDePedidosEntity nuevoDetalle = detallePedidoService.createDetallePedido(detallePedido);
        return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleDePedidosEntity> updateDetallePedido(@PathVariable int id, @RequestBody DetalleDePedidosEntity detallePedido) {
        detallePedido.setId(id);
        DetalleDePedidosEntity detalleActualizado = detallePedidoService.updateDetallePedido(detallePedido);
        return new ResponseEntity<>(detalleActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetallePedido(@PathVariable int id) {
        detallePedidoService.deleteDetallePedido(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/detalle")
    public ResponseEntity<DetalleDePedidosEntity> agregarDetalle(@RequestBody DetalleDePedidosEntity detalle) {
        try {
            detallePedidoService.insertarDetalleDePedido(detalle);
            return new ResponseEntity<>(detalle, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/productos-mas-pedidos-mes")
    public ResponseEntity<List<Map<String, Object>>> productosMasPedidosUltimoMes() {
        List<Map<String, Object>> result = detallePedidoService.productosMasPedidosPorCategoriaUltimoMes();
        return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(result);
    }


}