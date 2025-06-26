package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.entities.MedioDePagoEntity;
import com.tbd.DeliveryMedicamentos.services.MedioDePagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medios_de_pago")
public class MedioDePagoController {
    private final MedioDePagoService medioPagoService;

    @Autowired
    public MedioDePagoController(MedioDePagoService medioPagoService) {
        this.medioPagoService = medioPagoService;
    }

    @GetMapping
    public ResponseEntity<List<MedioDePagoEntity>> getAllMediosPago() {
        List<MedioDePagoEntity> mediosPago = medioPagoService.getAllMediosPago();
        return new ResponseEntity<>(mediosPago, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedioDePagoEntity> getMedioPagoById(@PathVariable int id) {
        MedioDePagoEntity medioPago = medioPagoService.getMedioPagoById(id);
        return new ResponseEntity<>(medioPago, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MedioDePagoEntity> createMedioPago(@RequestBody MedioDePagoEntity medioPago) {
        MedioDePagoEntity nuevoMedioPago = medioPagoService.createMedioPago(medioPago);
        return new ResponseEntity<>(nuevoMedioPago, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedioDePagoEntity> updateMedioPago(@PathVariable int id, @RequestBody MedioDePagoEntity medioPago) {
        medioPago.setId(id);
        MedioDePagoEntity medioPagoActualizado = medioPagoService.updateMedioPago(medioPago);
        return new ResponseEntity<>(medioPagoActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedioPago(@PathVariable int id) {
        medioPagoService.deleteMedioPago(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}