package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.DTO.PuntoConCoordenadasDTO;
import com.tbd.DeliveryMedicamentos.DTO.PuntoDTO;
import com.tbd.DeliveryMedicamentos.DTO.PuntoConDistanciaDTO;
import com.tbd.DeliveryMedicamentos.entities.PuntosEntity;
import com.tbd.DeliveryMedicamentos.services.PuntosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/puntos")
public class PuntosController {

    private final PuntosService puntosService;

    @Autowired
    public PuntosController(PuntosService puntosService) {
        this.puntosService = puntosService;
    }

    @PostMapping
    public ResponseEntity<PuntosEntity> createPunto(@RequestBody PuntoDTO dto) {
        PuntosEntity punto = new PuntosEntity(dto.getNombre(), dto.getIdFarmacia(), dto.getLatitud(), dto.getLongitud());
        PuntosEntity creado = puntosService.createPunto(punto, dto.getLatitud(), dto.getLongitud());
        return ResponseEntity.status(201).body(creado);
    }

    @GetMapping
    public List<PuntosEntity> getAll() {
        return puntosService.getAllPuntos();
    }

    @GetMapping("/coordenadas")
    public List<PuntoConCoordenadasDTO> getAllCoordenadas() {
        return puntosService.findAllPuntosConCoordenadas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PuntosEntity> getById(@PathVariable int id) {
        return puntosService.getPuntoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/coordenadas/{id}")
    public ResponseEntity<PuntoConCoordenadasDTO> getPuntoCoordenadasById(@PathVariable int id) {
        return puntosService.getPuntoConCoordenadasById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody PuntoDTO dto) {
        PuntosEntity punto = new PuntosEntity(dto.getNombre(), dto.getIdFarmacia(), dto.getLatitud(), dto.getLongitud());
        boolean updated = puntosService.updatePunto(id, punto, dto.getLatitud(), dto.getLongitud());
        return updated ? ResponseEntity.ok("Actualizado") : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        boolean deleted = puntosService.deletePunto(id);
        return deleted ? ResponseEntity.ok("Eliminado") : ResponseEntity.notFound().build();
    }



    // Metodos sentencias
    @GetMapping("/mas-lejanos")
    public List<PuntoConDistanciaDTO> obtenerPuntosMasLejanos() {
        return puntosService.obtenerPuntosMasLejanosPorFarmacia();
    }

    @GetMapping("/top5-cercanos")
    public List<PuntoConDistanciaDTO> obtenerTop5PuntosCercanos() {
        return puntosService.obtenerTop5PuntosCercanosPorFarmacia();
    }

}
