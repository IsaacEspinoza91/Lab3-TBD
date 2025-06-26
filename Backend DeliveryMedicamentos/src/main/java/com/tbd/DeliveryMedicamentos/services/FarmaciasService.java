package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.DTO.FarmaciaDTO;
import com.tbd.DeliveryMedicamentos.entities.FarmaciasEntity;
import com.tbd.DeliveryMedicamentos.repositories.FarmaciasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FarmaciasService {
    private final FarmaciasRepository farmaciaRepository;

    @Autowired
    public FarmaciasService(FarmaciasRepository farmaciaRepository) {
        this.farmaciaRepository = farmaciaRepository;
    }

    public List<FarmaciasEntity> getAllFarmacias() {
        return farmaciaRepository.findAll();
    }

    public FarmaciasEntity getFarmaciaById(int id) {
        return farmaciaRepository.findById(id);
    }

    public FarmaciaDTO getFarmaciaByIdConCoordenadas(int id) {
        return farmaciaRepository.findByIdConCoordenadas(id);
    }

    public FarmaciasEntity createFarmacia(FarmaciasEntity farmacia, Double Latitude, Double Longitude) {
        return farmaciaRepository.save(farmacia, Latitude, Longitude);
    }

    public FarmaciasEntity updateFarmacia(FarmaciasEntity farmacia) {
        farmaciaRepository.update(farmacia);
        return farmacia;
    }

    public void deleteFarmacia(int id) {
        farmaciaRepository.delete(id);
    }

    public long countFarmacias() {
        return farmaciaRepository.count();
    }

    public List<Map<String, Object>> obtenerFarmaciasMayorVolumenEntregado() {
        return farmaciaRepository.obtenerFarmaciasMayorVolumenEntregado();
    }

}