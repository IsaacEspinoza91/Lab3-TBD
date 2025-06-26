package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.DTO.RepartidorDistanciaMensualDTO;
import com.tbd.DeliveryMedicamentos.DTO.RepartidorRankingDTO;
import com.tbd.DeliveryMedicamentos.entities.RepartidorEntity;
import com.tbd.DeliveryMedicamentos.repositories.RepartidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RepartidorService {
    private final RepartidorRepository repartidorRepository;

    @Autowired
    public RepartidorService(RepartidorRepository repartidorRepository) {
        this.repartidorRepository = repartidorRepository;
    }
    public List<RepartidorEntity> getAllRepartidores() {
        return repartidorRepository.findAll();
    }

    public RepartidorEntity getRepartidorByUsuarioId(Integer usuarioId) {
        return repartidorRepository.findByUsuarioId(usuarioId);
    }

    public RepartidorEntity createRepartidor(RepartidorEntity repartidor) {
        return repartidorRepository.save(repartidor);
    }

    public RepartidorEntity updateRepartidor(RepartidorEntity repartidor) {
        repartidorRepository.update(repartidor);
        return repartidor;
    }

    public void deleteRepartidor(Integer usuarioId) {
        repartidorRepository.delete(usuarioId);
    }

    public List<Map<String, Object>> obtenerVistaDesempenoRepartidor() {
        return repartidorRepository.obtenerVistaDesempenoRepartidor();
    }

    public List<Map<String, Object>> obtenerTopRepartidores() {
        return repartidorRepository.obtenerTopRepartidores();
    }

    public List<RepartidorDistanciaMensualDTO> obtenerReporteDistanciaMensual() {
        return repartidorRepository.getDistanciaTotalRecorridaPorMes();
    }
}