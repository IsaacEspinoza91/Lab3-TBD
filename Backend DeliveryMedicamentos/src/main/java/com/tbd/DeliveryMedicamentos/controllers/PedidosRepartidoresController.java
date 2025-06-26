package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.entities.PedidosRepartidoresEntity;
import com.tbd.DeliveryMedicamentos.services.PedidosRepartidoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos_repartidores")
public class PedidosRepartidoresController {

    private final PedidosRepartidoresService pedidoRepartidorService;

    @Autowired
    public PedidosRepartidoresController(PedidosRepartidoresService pedidoRepartidorService) {
        this.pedidoRepartidorService = pedidoRepartidorService;
    }

    @GetMapping
    public ResponseEntity<List<PedidosRepartidoresEntity>> getAllPedidosRepartidores() {
        List<PedidosRepartidoresEntity> asignaciones = pedidoRepartidorService.getAllPedidosRepartidores();
        return new ResponseEntity<>(asignaciones, HttpStatus.OK);
    }

    @GetMapping("/repartidor/{repartidorId}")
    public ResponseEntity<List<PedidosRepartidoresEntity>> getPedidosByRepartidorId(@PathVariable int repartidorId) {
        List<PedidosRepartidoresEntity> asignaciones = pedidoRepartidorService.getPedidosByRepartidorId(repartidorId);
        return new ResponseEntity<>(asignaciones, HttpStatus.OK);
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<PedidosRepartidoresEntity>> getRepartidoresByPedidoId(@PathVariable int pedidoId) {
        List<PedidosRepartidoresEntity> asignaciones = pedidoRepartidorService.getRepartidoresByPedidoId(pedidoId);
        return new ResponseEntity<>(asignaciones, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> assignRepartidorToPedido(@RequestBody PedidosRepartidoresEntity pedidoRepartidor) {
        pedidoRepartidorService.assignRepartidorToPedido(pedidoRepartidor);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeRepartidorFromPedido(@RequestBody PedidosRepartidoresEntity pedidoRepartidor) {
        pedidoRepartidorService.removeRepartidorFromPedido(pedidoRepartidor);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
