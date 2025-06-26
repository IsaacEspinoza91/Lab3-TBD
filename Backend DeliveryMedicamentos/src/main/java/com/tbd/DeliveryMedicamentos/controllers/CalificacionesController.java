package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.entities.CalificacionesEntity;
import com.tbd.DeliveryMedicamentos.services.CalificacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionesController {
    private final CalificacionesService calificacionService;

    @Autowired
    public CalificacionesController(CalificacionesService calificacionService) {
        this.calificacionService = calificacionService;
    }

    @GetMapping
    public ResponseEntity<List<CalificacionesEntity>> getAllCalificaciones() {
        List<CalificacionesEntity> calificaciones = calificacionService.getAllCalificaciones();
        return new ResponseEntity<>(calificaciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalificacionesEntity> getCalificacionById(@PathVariable int id) {
        CalificacionesEntity calificacion = calificacionService.getCalificacionById(id);
        return new ResponseEntity<>(calificacion, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CalificacionesEntity> createCalificacion(@RequestBody CalificacionesEntity calificacion) {
        CalificacionesEntity nuevaCalificacion = calificacionService.createCalificacion(calificacion);
        return new ResponseEntity<>(nuevaCalificacion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalificacionesEntity> updateCalificacion(@PathVariable int id, @RequestBody CalificacionesEntity calificacion) {
        calificacion.setId(id);
        CalificacionesEntity calificacionActualizada = calificacionService.updateCalificacion(calificacion);
        return new ResponseEntity<>(calificacionActualizada, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalificacion(@PathVariable int id) {
        calificacionService.deleteCalificacion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}