package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.entities.CalificacionesEntity;
import com.tbd.DeliveryMedicamentos.repositories.CalificacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalificacionesService {
    private final CalificacionesRepository calificacionRepository;

    @Autowired
    public CalificacionesService(CalificacionesRepository calificacionRepository) {
        this.calificacionRepository = calificacionRepository;
    }

    public List<CalificacionesEntity> getAllCalificaciones() {
        return calificacionRepository.findAll();
    }

    public CalificacionesEntity getCalificacionById(int id) {
        return calificacionRepository.findById(id);
    }

    public CalificacionesEntity createCalificacion(CalificacionesEntity calificacion) {
        return calificacionRepository.save(calificacion);
    }

    public CalificacionesEntity updateCalificacion(CalificacionesEntity calificacion) {
        calificacionRepository.update(calificacion);
        return calificacion;
    }

    public void deleteCalificacion(int id) {
        calificacionRepository.delete(id);
    }
}