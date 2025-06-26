package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.DTO.FarmaciaDTO;
import com.tbd.DeliveryMedicamentos.entities.FarmaciasEntity;
import com.tbd.DeliveryMedicamentos.services.FarmaciasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/farmacias")
public class FarmaciasController {
    private final FarmaciasService farmaciaService;

    @Autowired
    public FarmaciasController(FarmaciasService farmaciaService) {
        this.farmaciaService = farmaciaService;
    }

    @GetMapping
    public ResponseEntity<List<FarmaciasEntity>> getAllFarmacias() {
        List<FarmaciasEntity> farmacias = farmaciaService.getAllFarmacias();
        return new ResponseEntity<>(farmacias, HttpStatus.OK);
    }

    @GetMapping("/volumen-entregado")
    public ResponseEntity<List<Map<String, Object>>> getFarmaciasMayorVolumenEntregado() {
        return ResponseEntity.ok(farmaciaService.obtenerFarmaciasMayorVolumenEntregado());
    }


    @GetMapping("/{id}")
    public ResponseEntity<FarmaciasEntity> getFarmaciaById(@PathVariable int id) {
        FarmaciasEntity farmacia = farmaciaService.getFarmaciaById(id);
        return new ResponseEntity<>(farmacia, HttpStatus.OK);
    }

    @GetMapping("/coordenadas/{id}")
    public ResponseEntity<FarmaciaDTO> getFarmaciaConCoordenadasById(@PathVariable int id) {
        FarmaciaDTO farmacia = farmaciaService.getFarmaciaByIdConCoordenadas(id);
        return new ResponseEntity<>(farmacia, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FarmaciasEntity> createFarmacia(@RequestBody FarmaciaDTO farmaciaDTO) {
        FarmaciasEntity farmacia = new FarmaciasEntity();
        farmacia.setNombre(farmaciaDTO.getNombre());
        farmacia.setLugar(farmaciaDTO.getLugar());
        FarmaciasEntity nuevaFarmacia = farmaciaService.createFarmacia(farmacia, farmaciaDTO.getLatitud(), farmaciaDTO.getLongitud());
        return new ResponseEntity<>(nuevaFarmacia, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FarmaciasEntity> updateFarmacia(@PathVariable int id, @RequestBody FarmaciasEntity farmacia) {
        farmacia.setId(id);
        FarmaciasEntity farmaciaActualizada = farmaciaService.updateFarmacia(farmacia);
        return new ResponseEntity<>(farmaciaActualizada, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmacia(@PathVariable int id) {
        farmaciaService.deleteFarmacia(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/contar")
    public ResponseEntity<Map<String, Long>> contarFarmacias() {
        long numFarmacias = farmaciaService.countFarmacias();
        return ResponseEntity.ok(Collections.singletonMap("count", numFarmacias));
    }


}