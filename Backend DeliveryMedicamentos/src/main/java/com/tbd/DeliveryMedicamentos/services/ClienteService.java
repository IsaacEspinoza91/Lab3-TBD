package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.DTO.ClienteDetalladoDTO;
import com.tbd.DeliveryMedicamentos.DTO.ClienteTopGastoDTO;
import com.tbd.DeliveryMedicamentos.DTO.ResumenPedidoClienteDTO;
import com.tbd.DeliveryMedicamentos.entities.ClienteEntity;
import com.tbd.DeliveryMedicamentos.entities.UsuarioEntity;
import com.tbd.DeliveryMedicamentos.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteEntity> getAllClientes() {
        return clienteRepository.findAll();
    }

    public ClienteEntity getClienteByUsuarioId(Integer usuarioId) {
        return clienteRepository.findByUsuarioId(usuarioId);
    }



    public ClienteEntity createCliente(ClienteEntity cliente) {
        return clienteRepository.save(cliente);
    }

    /*
    public void crearClienteCompleto(UsuarioEntity usuario, ClienteEntity cliente) {
        // Primero guardar el usuario
        usuario.setTipo("CLIENTE");
        UsuarioEntity usuarioGuardado = usuarioRepository.save(usuario);

        // Luego guardar los datos específicos del cliente
        cliente.setUsuarioId(usuarioGuardado.getId());
        clienteRepository.save(cliente);
    }

    @Transactional
    public void updateClienteCompleto(UsuarioEntity usuario, ClienteEntity cliente) {
        usuarioRepository.update(usuario);
        clienteRepository.update(cliente);
    }

    @Transactional
    public void deleteClienteCompleto(Integer usuarioId) {
        clienteRepository.delete(usuarioId);
        usuarioRepository.delete(usuarioId);
    }*/

    public ClienteEntity updateCliente(ClienteEntity cliente) {
        clienteRepository.update(cliente);
        return cliente;
    }

    public List<ClienteDetalladoDTO> getClientesDetallados() {
        return clienteRepository.obtenerClientesDetallados();
    }

    public Map<String, Object> clienteQueMasGasto() {
        return clienteRepository.obtenerClienteQueMasGasto();
    }

    public List<ResumenPedidoClienteDTO> obtenerResumenPedidos() {
        return clienteRepository.getResumenPedidos();
    }

    public void deleteCliente(Integer usuarioId) {
        clienteRepository.delete(usuarioId);
    }

    public ClienteTopGastoDTO findClienteMayorGasto() {
        return clienteRepository.findClienteMayorGasto();
    }

    public List<ClienteDetalladoDTO> getClientesLejanosA5kmDeFarmacia() {
        return clienteRepository.findClientesLejanosA5kmDeFarmacia();
    }
}