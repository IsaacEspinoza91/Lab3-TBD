package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.entities.FarmaciasProductosEntity;
import com.tbd.DeliveryMedicamentos.repositories.FarmaciasProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FarmaciasProductosService {
    private final FarmaciasProductosRepository farmaciaProductoRepository;

    @Autowired
    public FarmaciasProductosService(FarmaciasProductosRepository farmaciaProductoRepository) {
        this.farmaciaProductoRepository = farmaciaProductoRepository;
    }

    public List<FarmaciasProductosEntity> getAllFarmaciaProductos() {
        return farmaciaProductoRepository.findAll();
    }

    public List<FarmaciasProductosEntity> getProductosByFarmaciaId(int farmaciaId) {
        return farmaciaProductoRepository.findByFarmaciaId(farmaciaId);
    }

    public List<FarmaciasProductosEntity> getFarmaciasByProductoId(int productoId) {
        return farmaciaProductoRepository.findByProductoId(productoId);
    }

    public void addProductToFarmacia(FarmaciasProductosEntity farmaciaProducto) {
        farmaciaProductoRepository.save(farmaciaProducto);
    }

    public void removeProductFromFarmacia(FarmaciasProductosEntity farmaciaProducto) {
        farmaciaProductoRepository.delete(farmaciaProducto);
    }
}