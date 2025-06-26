package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.DTO.RepartidorDistanciaMensualDTO;
import com.tbd.DeliveryMedicamentos.DTO.RepartidorRankingDTO;
import com.tbd.DeliveryMedicamentos.entities.RepartidorEntity;
import com.tbd.DeliveryMedicamentos.services.RepartidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/repartidores")
public class RepartidorController {
    private final RepartidorService repartidorService;

    @Autowired
    public RepartidorController(RepartidorService repartidorService) {
        this.repartidorService = repartidorService;
    }

    @GetMapping
    public ResponseEntity<List<RepartidorEntity>> getAllRepartidores() {
        List<RepartidorEntity> repartidores = repartidorService.getAllRepartidores();
        return new ResponseEntity<>(repartidores, HttpStatus.OK);
    }

    @GetMapping("/top")
    public List<Map<String, Object>> obtenerTopRepartidores() {
        return repartidorService.obtenerTopRepartidores();
    }

    @GetMapping("/desempeno")
    public List<Map<String, Object>> obtenerVistaDesempenoRepartidor() {
        return repartidorService.obtenerVistaDesempenoRepartidor();
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<RepartidorEntity> getRepartidorByUsuarioId(@PathVariable int usuarioId) {
        RepartidorEntity repartidor = repartidorService.getRepartidorByUsuarioId(usuarioId);
        return new ResponseEntity<>(repartidor, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RepartidorEntity> createRepartidor(@RequestBody RepartidorEntity repartidor) {
        RepartidorEntity nuevoRepartidor = repartidorService.createRepartidor(repartidor);
        return new ResponseEntity<>(nuevoRepartidor, HttpStatus.CREATED);
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<RepartidorEntity> updateRepartidor(@PathVariable int usuarioId, @RequestBody RepartidorEntity repartidor) {
        repartidor.setUsuario_id(usuarioId);
        RepartidorEntity repartidorActualizado = repartidorService.updateRepartidor(repartidor);
        return new ResponseEntity<>(repartidorActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> deleteRepartidor(@PathVariable int usuarioId) {
        repartidorService.deleteRepartidor(usuarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/distancia-mensual")
    public List<RepartidorDistanciaMensualDTO> getDistanciaMensual() {
        return repartidorService.obtenerReporteDistanciaMensual();
    }
}