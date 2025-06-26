package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.entities.MedioDePagoEntity;
import com.tbd.DeliveryMedicamentos.repositories.MedioDePagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedioDePagoService {
    private final MedioDePagoRepository medioPagoRepository;

    @Autowired
    public MedioDePagoService(MedioDePagoRepository medioPagoRepository) {
        this.medioPagoRepository = medioPagoRepository;
    }

    public List<MedioDePagoEntity> getAllMediosPago() {
        return medioPagoRepository.findAll();
    }

    public MedioDePagoEntity getMedioPagoById(int id) {
        return medioPagoRepository.findById(id);
    }

    public MedioDePagoEntity createMedioPago(MedioDePagoEntity medioPago) {
        return medioPagoRepository.save(medioPago);
    }

    public MedioDePagoEntity updateMedioPago(MedioDePagoEntity medioPago) {
        medioPagoRepository.update(medioPago);
        return medioPago;
    }

    public void deleteMedioPago(int id) {
        medioPagoRepository.delete(id);
    }
}