package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.DTO.PtoInteresDTO; // Asegúrate de importar tu DTO
import com.tbd.DeliveryMedicamentos.entities.PtoInteresEntity; // Asegúrate de importar tu Entity
import com.tbd.DeliveryMedicamentos.repositories.PtoInteresRepository; // Asegúrate de importar tu Repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PtoInteresService {
    private final PtoInteresRepository ptoInteresRepository;

    @Autowired
    public PtoInteresService(PtoInteresRepository ptoInteresRepository) {
        this.ptoInteresRepository = ptoInteresRepository;
    }

    public List<PtoInteresEntity> getAllPtosInteres() {
        return ptoInteresRepository.findAll();
    }

    public PtoInteresEntity getPtoInteresById(int id) {
        return ptoInteresRepository.findById(id);
    }

    public PtoInteresDTO getPtoInteresByIdConCoordenadas(int id) {
        return ptoInteresRepository.findByIdConCoordenadas(id);
    }

    public PtoInteresEntity createPtoInteres(PtoInteresEntity ptoInteres, Double latitud, Double longitud) {
        // Aquí podrías añadir lógica de negocio adicional antes de guardar,
        // como validaciones más complejas o transformaciones.
        return ptoInteresRepository.save(ptoInteres, latitud, longitud);
    }

    public PtoInteresEntity updatePtoInteres(PtoInteresEntity ptoInteres, Double latitud, Double longitud) {
        // También podrías añadir lógica de negocio para la actualización.
        // El repositorio ya maneja la lógica de actualizar el geom si las coordenadas son nulas.
        ptoInteresRepository.update(ptoInteres, latitud, longitud);
        return ptoInteres; // Retorna la entidad actualizada
    }

    public void deletePtoInteres(int id) {
        ptoInteresRepository.delete(id);
    }

    public long countPtosInteres() {
        return ptoInteresRepository.count();
    }

    public List<PtoInteresDTO> getPtosInteresCercanosAUsuario(Integer idUsuario) {
        return ptoInteresRepository.findPtosInteresCercanosAUsuario(idUsuario);
    }

    // Si tuvieras métodos adicionales en el repositorio para puntos de interés,
    // como buscar puntos cercanos, los delegarías aquí:
    /*
    public List<PtoInteresDTO> findPtosInteresCercanos(Double latitud, Double longitud, Double radioMetros) {
        // Aquí podrías añadir lógica de negocio, como validar el radio o las coordenadas.
        return ptoInteresRepository.findPuntosCercanos(latitud, longitud, radioMetros);
    }
    */
}