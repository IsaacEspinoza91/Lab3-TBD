package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.entities.AdministradorEntity;
import com.tbd.DeliveryMedicamentos.repositories.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AdministradorService {
    private final AdministradorRepository administradorRepository;

    @Autowired
    public AdministradorService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    public List<AdministradorEntity> getAllAdministradores() {
        return administradorRepository.findAll();
    }

    public AdministradorEntity getAdministradorByUsuarioId(Integer usuarioId) {
        return administradorRepository.findByUsuarioId(usuarioId);
    }

    public AdministradorEntity createAdministrador(AdministradorEntity administrador) {
        return administradorRepository.save(administrador);
    }

    public AdministradorEntity updateAdministrador(AdministradorEntity administrador) {
        administradorRepository.update(administrador);
        return administrador;
    }

    public void deleteAdministrador(Integer usuarioId) {
        administradorRepository.delete(usuarioId);
    }
}