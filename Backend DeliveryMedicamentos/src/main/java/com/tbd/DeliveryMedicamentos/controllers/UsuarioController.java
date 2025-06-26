package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.DTO.ZonaCoberturaDTO;
import com.tbd.DeliveryMedicamentos.DTO.ZonaUsuarioDTO;
import com.tbd.DeliveryMedicamentos.entities.UsuarioEntity;
import com.tbd.DeliveryMedicamentos.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/zona/{id}")
    public ResponseEntity<ZonaUsuarioDTO> getZonaCliente(@PathVariable int id) {
        ZonaUsuarioDTO zona = usuarioService.getZonaDeCliente(id);
        if (zona != null) {
            return new ResponseEntity<>(zona, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/ver-zona/{id}")
    public ResponseEntity<ZonaUsuarioDTO> verZonaUsuario(@PathVariable int id) {
        ZonaUsuarioDTO zona = usuarioService.verZonaUser(id);
        if (zona != null) {
            return new ResponseEntity<>(zona, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<UsuarioEntity> createUsuario(@RequestBody UsuarioEntity usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        UsuarioEntity nuevoUsuario = usuarioService.createUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<UsuarioEntity>> getAllUsuarios() {
        List<UsuarioEntity> usuarios = usuarioService.getAllUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> getUsuarioById(@PathVariable Integer id) {
        UsuarioEntity usuario = usuarioService.getUsuarioById(id);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioEntity> getUsuarioByEmail(@PathVariable String email) {
        UsuarioEntity usuario = usuarioService.getUsuarioByEmail(email);
        return usuario != null ?
                ResponseEntity.ok(usuario) :
                ResponseEntity.notFound().build();
    }

    /*
    @PostMapping
    public ResponseEntity<UsuarioEntity> createUsuario(@RequestBody UsuarioEntity usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        UsuarioEntity nuevoUsuario = usuarioService.createUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }*/

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioEntity> updateUsuario(@PathVariable Integer id,
                                                       @RequestBody UsuarioEntity usuario) {
        usuario.setId(id);
        UsuarioEntity usuarioActualizado = usuarioService.updateUsuario(usuario);
        return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        usuarioService.deleteUsuario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/iniciarSesion")
    public ResponseEntity<?> loginUsuario(@RequestParam String email, @RequestParam String password) {
        UsuarioEntity usuario = usuarioService.getUsuarioByEmail(email);

        if (usuario != null && usuario.getPassword().equals(password)) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", usuario.getId());
            response.put("tipo", usuario.getTipo());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Email o contraseña incorrectos.");
        }
    }

    @GetMapping("/contar")
    public ResponseEntity<Map<String, Long>> contarUsuarios() {
        long numUsers = usuarioService.countUsuarios();
        return ResponseEntity.ok(Collections.singletonMap("count", numUsers));
    }

    @GetMapping("/{id}/zonas")
    public ResponseEntity<?> obtenerZonasPorUsuario(@PathVariable("id") int usuarioId) {
        try {
            List<ZonaCoberturaDTO> zonas = usuarioService.obtenerZonasDeUsuario(usuarioId);
            return ResponseEntity.ok(zonas);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}