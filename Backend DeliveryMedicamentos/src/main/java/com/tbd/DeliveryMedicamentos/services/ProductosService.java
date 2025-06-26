package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.DTO.RankingProductosCanceladosDTO;
import com.tbd.DeliveryMedicamentos.DTO.RankingProductosDevueltosDTO;
import com.tbd.DeliveryMedicamentos.entities.ProductosEntity;
import com.tbd.DeliveryMedicamentos.repositories.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductosService {
    private final ProductosRepository productoRepository;

    @Autowired
    public ProductosService(ProductosRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<ProductosEntity> getAllProductos() {
        return productoRepository.findAll();
    }

    public ProductosEntity getProductoById(int id) {
        return productoRepository.findById(id);
    }

    public ProductosEntity createProducto(ProductosEntity producto) {
        return productoRepository.save(producto);
    }

    public ProductosEntity updateProducto(ProductosEntity producto) {
        productoRepository.update(producto);
        return producto;
    }

    public void deleteProducto(int id) {
        productoRepository.delete(id);
    }

    public long countProductos() {
        return productoRepository.count();
    }

    public List<RankingProductosCanceladosDTO> findProductosMasCancelados() {
        return productoRepository.findProductosMasCancelados();
    }

    public List<RankingProductosDevueltosDTO> findProductosMasDevueltos() {
        return productoRepository.findProductosMasDevueltos();
    }
}