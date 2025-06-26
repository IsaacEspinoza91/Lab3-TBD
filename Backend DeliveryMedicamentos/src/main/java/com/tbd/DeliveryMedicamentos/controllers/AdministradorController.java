package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.entities.AdministradorEntity;
import com.tbd.DeliveryMedicamentos.services.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administradores")
public class AdministradorController {
    private final AdministradorService administradorService;

    @Autowired
    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @GetMapping
    public ResponseEntity<List<AdministradorEntity>> getAllAdministradores() {
        List<AdministradorEntity> administradores = administradorService.getAllAdministradores();
        return new ResponseEntity<>(administradores, HttpStatus.OK);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<AdministradorEntity> getAdministradorByUsuarioId(@PathVariable int usuarioId) {
        AdministradorEntity administrador = administradorService.getAdministradorByUsuarioId(usuarioId);
        return new ResponseEntity<>(administrador, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AdministradorEntity> createAdministrador(@RequestBody AdministradorEntity administrador) {
        AdministradorEntity nuevoAdministrador = administradorService.createAdministrador(administrador);
        return new ResponseEntity<>(nuevoAdministrador, HttpStatus.CREATED);
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<AdministradorEntity> updateAdministrador(@PathVariable int usuarioId, @RequestBody AdministradorEntity administrador) {
        administrador.setUsuario_id(usuarioId);
        AdministradorEntity administradorActualizado = administradorService.updateAdministrador(administrador);
        return new ResponseEntity<>(administradorActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> deleteAdministrador(@PathVariable int usuarioId) {
        administradorService.deleteAdministrador(usuarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}