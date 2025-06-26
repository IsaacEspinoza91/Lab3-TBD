package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.DTO.PtoInteresDTO; // Asegúrate de importar tu DTO
import com.tbd.DeliveryMedicamentos.entities.PtoInteresEntity; // Asegúrate de importar tu Entity
import com.tbd.DeliveryMedicamentos.services.PtoInteresService; // Asegúrate de importar tu Service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/puntos-de-interes") // Define el path base para este controlador
public class PtoInteresController {
    private final PtoInteresService ptoInteresService;

    @Autowired
    public PtoInteresController(PtoInteresService ptoInteresService) {
        this.ptoInteresService = ptoInteresService;
    }

    @GetMapping
    public ResponseEntity<List<PtoInteresEntity>> getAllPtosInteres() {
        List<PtoInteresEntity> puntos = ptoInteresService.getAllPtosInteres();
        return new ResponseEntity<>(puntos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PtoInteresEntity> getPtoInteresById(@PathVariable int id) {
        PtoInteresEntity ptoInteres = ptoInteresService.getPtoInteresById(id);
        if (ptoInteres != null) {
            return new ResponseEntity<>(ptoInteres, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/coordenadas/{id}")
    public ResponseEntity<PtoInteresDTO> getPtoInteresConCoordenadasById(@PathVariable int id) {
        PtoInteresDTO ptoInteres = ptoInteresService.getPtoInteresByIdConCoordenadas(id);
        if (ptoInteres != null) {
            return new ResponseEntity<>(ptoInteres, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<PtoInteresEntity> createPtoInteres(@RequestBody PtoInteresDTO ptoInteresDTO) {
        // Mapeamos el DTO a la Entity para que el servicio pueda manejar la persistencia.
        // El campo 'geom' se establecerá dentro del servicio/repositorio.
        PtoInteresEntity ptoInteres = new PtoInteresEntity();
        ptoInteres.setNombre(ptoInteresDTO.getNombre());
        ptoInteres.setLugar(ptoInteresDTO.getLugar());

        PtoInteresEntity nuevoPtoInteres = ptoInteresService.createPtoInteres(
                ptoInteres,
                ptoInteresDTO.getLatitud(),
                ptoInteresDTO.getLongitud()
        );
        return new ResponseEntity<>(nuevoPtoInteres, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PtoInteresEntity> updatePtoInteres(@PathVariable int id, @RequestBody PtoInteresDTO ptoInteresDTO) {
        PtoInteresEntity ptoInteres = new PtoInteresEntity();
        ptoInteres.setId(id); // Establece el ID de la URL
        ptoInteres.setNombre(ptoInteresDTO.getNombre());
        ptoInteres.setLugar(ptoInteresDTO.getLugar());

        PtoInteresEntity ptoInteresActualizado = ptoInteresService.updatePtoInteres(
                ptoInteres,
                ptoInteresDTO.getLatitud(),
                ptoInteresDTO.getLongitud()
        );

        if (ptoInteresActualizado != null) {
            return new ResponseEntity<>(ptoInteresActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePtoInteres(@PathVariable int id) {
        ptoInteresService.deletePtoInteres(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/contar")
    public ResponseEntity<Map<String, Long>> countPtosInteres() {
        long numPtosInteres = ptoInteresService.countPtosInteres();
        return ResponseEntity.ok(Collections.singletonMap("count", numPtosInteres));
    }

    @GetMapping("/cercanos-a-usuario/{idUsuario}")
    public ResponseEntity<List<PtoInteresDTO>> getPtosInteresCercanosAUsuario(@PathVariable int idUsuario) {
        List<PtoInteresDTO> puntosCercanos = ptoInteresService.getPtosInteresCercanosAUsuario(idUsuario);

        // Retorna 200 OK con la lista (vacía o con contenido)
        return new ResponseEntity<>(puntosCercanos, HttpStatus.OK);
    }
}