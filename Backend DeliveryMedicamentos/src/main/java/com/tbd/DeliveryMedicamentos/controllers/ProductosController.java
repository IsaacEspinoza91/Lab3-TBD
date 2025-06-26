package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.DTO.RankingProductosCanceladosDTO;
import com.tbd.DeliveryMedicamentos.DTO.RankingProductosDevueltosDTO;
import com.tbd.DeliveryMedicamentos.entities.ProductosEntity;
import com.tbd.DeliveryMedicamentos.services.ProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
public class ProductosController {
    private final ProductosService productoService;

    @Autowired
    public ProductosController(ProductosService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductosEntity>> getAllProductos() {
        List<ProductosEntity> productos = productoService.getAllProductos();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductosEntity> getProductoById(@PathVariable int id) {
        ProductosEntity producto = productoService.getProductoById(id);
        return new ResponseEntity<>(producto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductosEntity> createProducto(@RequestBody ProductosEntity producto) {
        ProductosEntity nuevoProducto = productoService.createProducto(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductosEntity> updateProducto(@PathVariable int id, @RequestBody ProductosEntity producto) {
        producto.setId(id);
        ProductosEntity productoActualizado = productoService.updateProducto(producto);
        return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable int id) {
        productoService.deleteProducto(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/contar")
    public ResponseEntity<Map<String, Long>> contarProductos() {
        long num = productoService.countProductos();
        return ResponseEntity.ok(Collections.singletonMap("count", num));
    }

    @GetMapping("/top-productos-cancelados")
    public ResponseEntity<List<RankingProductosCanceladosDTO>> getProductosMasCancelados() {
        List<RankingProductosCanceladosDTO> ranking = productoService.findProductosMasCancelados();
        return ResponseEntity.ok(ranking);
    }

    @GetMapping("/top-productos-devueltos")
    public ResponseEntity<List<RankingProductosDevueltosDTO>> getProductosMasDevueltos() {
        List<RankingProductosDevueltosDTO> ranking = productoService.findProductosMasDevueltos();
        return ResponseEntity.ok(ranking);
    }
}