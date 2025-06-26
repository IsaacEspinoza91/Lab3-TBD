package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.entities.DetalleDePedidosEntity;
import com.tbd.DeliveryMedicamentos.repositories.DetalleDePedidosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DetalleDePedidosService {
    private final DetalleDePedidosRepository detallePedidoRepository;

    @Autowired
    public DetalleDePedidosService(DetalleDePedidosRepository detallePedidoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
    }

    public List<DetalleDePedidosEntity> getAllDetallesPedido() {
        return detallePedidoRepository.findAll();
    }

    public List<DetalleDePedidosEntity> getDetallesByPedidoId(int pedidoId) {
        return detallePedidoRepository.findByPedidoId(pedidoId);
    }

    public DetalleDePedidosEntity getDetallePedidoById(int id) {
        return detallePedidoRepository.findById(id);
    }

    public DetalleDePedidosEntity createDetallePedido(DetalleDePedidosEntity detallePedido) {
        return detallePedidoRepository.save(detallePedido);
    }

    public DetalleDePedidosEntity updateDetallePedido(DetalleDePedidosEntity detallePedido) {
        detallePedidoRepository.update(detallePedido);
        return detallePedido;
    }

    public void deleteDetallePedido(int id) {
        detallePedidoRepository.delete(id);
    }

    public void insertarDetalleDePedido(DetalleDePedidosEntity detalle) {
        detallePedidoRepository.insertarDetalleDePedido(detalle);
    }

    public List<Map<String, Object>> productosMasPedidosPorCategoriaUltimoMes() {
        return detallePedidoRepository.productosMasPedidosPorCategoriaUltimoMes();
    }

}