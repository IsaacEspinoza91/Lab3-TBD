package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.DTO.DetallePedidoDTO;
import com.tbd.DeliveryMedicamentos.DTO.PedidosDTO;
import com.tbd.DeliveryMedicamentos.DTO.ResumenPedidoClienteDTO;
import com.tbd.DeliveryMedicamentos.DTO.RutasZonasCruzadasDTO;
import com.tbd.DeliveryMedicamentos.entities.PedidosEntity;
import com.tbd.DeliveryMedicamentos.repositories.PedidosRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PedidosService {
    private final PedidosRepository pedidoRepository;

    public PedidosService(PedidosRepository repository) {
        this.pedidoRepository = repository;
    }

    public List<RutasZonasCruzadasDTO> rutasZonasCruzadas() {
        return pedidoRepository.rutasZonasCruzadas();
    }


    public List<PedidosEntity> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public String mostrarRuta(int pedidoId) {
        return pedidoRepository.mostrarRuta(pedidoId);
    }

    public String mostrarRutaMultiLineString(int pedidoId) {
        return pedidoRepository.mostrarRutaMultiLineString(pedidoId);
    }


    public PedidosEntity getPedidoById(Integer id) {
        return pedidoRepository.findById(id);
    }

    public PedidosEntity createPedido(PedidosDTO pedidoDTO) {
        String rutaEstimada = convertToLineString(pedidoDTO.getCoordenadas()); // Convertir coordenadas a LINESTRING

        PedidosEntity pedido = new PedidosEntity(
                0, // ID se generará en DB
                pedidoDTO.getFecha(),
                pedidoDTO.getUrgencia(),
                pedidoDTO.getTotal_pagado(),
                pedidoDTO.getEstado_entrega(),
                pedidoDTO.getFecha_entrega(),
                pedidoDTO.getCliente_id(),
                pedidoDTO.getMedio_pago_id(),
                pedidoDTO.getFarmacia_id(),
                pedidoDTO.getRepartidor_id(),
                rutaEstimada
        );

        pedido.setRutaEstimada(rutaEstimada); // Setear la ruta
        return pedidoRepository.save(pedido);
    }

    // Método para convertir lista de coordenadas en LINESTRING
    private String convertToLineString(List<Double[]> coordenadas) {
        if (coordenadas == null || coordenadas.isEmpty()) {
            return null;
        }

        StringBuilder linea = new StringBuilder("LINESTRING(");
        for (Double[] punto : coordenadas) {
            linea.append(punto[1]).append(" ").append(punto[0]).append(","); // lng lat (PostGIS usa X,Y)
        }
        linea.deleteCharAt(linea.length() - 1); // Remover última coma
        linea.append(")");
        return linea.toString();
    }

    public void registrarPedido(PedidosEntity pedido, List<DetallePedidoDTO> detalles) {
        pedidoRepository.registrarPedido(pedido, detalles);
    }

    public List<ResumenPedidoClienteDTO> obtenerResumen() {
        return pedidoRepository.obtenerResumenPedidos();
    }

    public PedidosEntity updatePedido(PedidosEntity pedido) {
        pedidoRepository.update(pedido);
        return pedidoRepository.findById(pedido.getId());
    }

    public void deletePedido(Integer id) {
        pedidoRepository.delete(id);
    }

    public List<Map<String, Object>> tiempoPromedioPorRepartidor() {
        return pedidoRepository.tiempoPromedioPorRepartidor();
    }

    public List<Map<String, Object>> tiempoPromedioPorRepartidorId(Integer id) {
        return pedidoRepository.tiempoPromedioPorRepartidorId(id);
    }

    public void cambiarEstadoPedido(int idPedido, String nuevoEstado) {
        pedidoRepository.cambiarEstadoPedido(idPedido, nuevoEstado);
    }

    public Map<String, Object> medioPagoMasUsadoEnUrgencias() {
        return pedidoRepository.medioPagoMasUsadoEnUrgencias();
    }

    public long contarPedidosMesActual() {
        return pedidoRepository.countPedidosMesActual();
    }

    public List<Map<String, Object>> farmaciasConMasEntregasFallidas() {
        return pedidoRepository.farmaciasConMasEntregasFallidas();
    }

    public Map<String, Object> clienteConMasGastoEnPedidosEntregados() {
        return pedidoRepository.clienteConMasGastoEnPedidosEntregados();
    }

    public Map<String, Object> obtenerMedioPagoMasUsadoEnUrgentes() {
        return pedidoRepository.obtenerMedioPagoMasUsadoEnUrgentes();
    }

    public List<Integer> obtenerPedidosConNotificaciones() {
        return pedidoRepository.obtenerPedidosConNotificaciones();
    }

    public boolean actualizarEstadoEntregaPedido(int pedidoId, String nuevoEstado) {
        return pedidoRepository.actualizarEstadoEntrega(pedidoId, nuevoEstado);
    }

    public List<Integer> obtenerPedidosTardados() {
        return pedidoRepository.obtenerPedidosTardados();
    }

    public void descontarStock(int pedidoId) {
        pedidoRepository.descontarStockConfirmado(pedidoId);
    }


}