package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.entities.FarmaciasProductosEntity;
import com.tbd.DeliveryMedicamentos.services.FarmaciasProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmacias_productos")
public class FarmaciasProductosController {
    private final FarmaciasProductosService farmaciaProductoService;

    @Autowired
    public FarmaciasProductosController(FarmaciasProductosService farmaciaProductoService) {
        this.farmaciaProductoService = farmaciaProductoService;
    }

    @GetMapping
    public ResponseEntity<List<FarmaciasProductosEntity>> getAllFarmaciaProductos() {
        List<FarmaciasProductosEntity> asignaciones = farmaciaProductoService.getAllFarmaciaProductos();
        return new ResponseEntity<>(asignaciones, HttpStatus.OK);
    }

    @GetMapping("/farmacia/{farmaciaId}")
    public ResponseEntity<List<FarmaciasProductosEntity>> getProductosByFarmaciaId(@PathVariable int farmaciaId) {
        List<FarmaciasProductosEntity> asignaciones = farmaciaProductoService.getProductosByFarmaciaId(farmaciaId);
        return new ResponseEntity<>(asignaciones, HttpStatus.OK);
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<FarmaciasProductosEntity>> getFarmaciasByProductoId(@PathVariable int productoId) {
        List<FarmaciasProductosEntity> asignaciones = farmaciaProductoService.getFarmaciasByProductoId(productoId);
        return new ResponseEntity<>(asignaciones, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addProductToFarmacia(@RequestBody FarmaciasProductosEntity farmaciaProducto) {
        farmaciaProductoService.addProductToFarmacia(farmaciaProducto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeProductFromFarmacia(@RequestBody FarmaciasProductosEntity farmaciaProducto) {
        farmaciaProductoService.removeProductFromFarmacia(farmaciaProducto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}