package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.DTO.ZonaDensidadDTO;
import com.tbd.DeliveryMedicamentos.entities.ZonasEntity;
import com.tbd.DeliveryMedicamentos.repositories.ZonasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ZonasService {

    private final ZonasRepository zonasRepository;

    @Autowired
    public ZonasService(ZonasRepository zonasRepository) {
        this.zonasRepository = zonasRepository;
    }

    public List<ZonasEntity> findAll() {
        return zonasRepository.findAll();
    }

    public Optional<ZonasEntity> findById(int id) {
        return zonasRepository.findById(id);
    }

    public ZonasEntity save(ZonasEntity zona) {
        return zonasRepository.save(zona);
    }

    public boolean update(int id, ZonasEntity zona) {
        return zonasRepository.update(id, zona);
    }

    public boolean delete(int id) {
        return zonasRepository.delete(id);
    }

    public List<ZonaDensidadDTO> getZonasConAltaDensidadPedidos() {
        return zonasRepository.findZonasConAltaDensidadPedidos();
    }
}
