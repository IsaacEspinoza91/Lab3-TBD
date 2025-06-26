package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.entities.PedidosRepartidoresEntity;
import com.tbd.DeliveryMedicamentos.repositories.PedidosRepartidoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidosRepartidoresService {
    private final PedidosRepartidoresRepository pedidoRepartidorRepository;

    @Autowired
    public PedidosRepartidoresService(PedidosRepartidoresRepository pedidoRepartidorRepository) {
        this.pedidoRepartidorRepository = pedidoRepartidorRepository;
    }

    public List<PedidosRepartidoresEntity> getAllPedidosRepartidores() {
        return pedidoRepartidorRepository.findAll();
    }

    public List<PedidosRepartidoresEntity> getPedidosByRepartidorId(int repartidorId) {
        return pedidoRepartidorRepository.findByRepartidorId(repartidorId);
    }

    public List<PedidosRepartidoresEntity> getRepartidoresByPedidoId(int pedidoId) {
        return pedidoRepartidorRepository.findByPedidoId(pedidoId);
    }

    public void assignRepartidorToPedido(PedidosRepartidoresEntity pedidoRepartidor) {
        pedidoRepartidorRepository.save(pedidoRepartidor);
    }

    public void removeRepartidorFromPedido(PedidosRepartidoresEntity pedidoRepartidor) {
        pedidoRepartidorRepository.delete(pedidoRepartidor);
    }
}
