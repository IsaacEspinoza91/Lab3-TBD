package com.tbd.DeliveryMedicamentos.services;

import com.tbd.DeliveryMedicamentos.DTO.ZonaCoberturaDTO;
import com.tbd.DeliveryMedicamentos.DTO.ZonaUsuarioDTO;
import com.tbd.DeliveryMedicamentos.entities.AdministradorEntity;
import com.tbd.DeliveryMedicamentos.entities.ClienteEntity;
import com.tbd.DeliveryMedicamentos.entities.RepartidorEntity;
import com.tbd.DeliveryMedicamentos.entities.UsuarioEntity;
import com.tbd.DeliveryMedicamentos.repositories.RepartidorRepository;
import com.tbd.DeliveryMedicamentos.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final AdministradorService administradorService;
    private final RepartidorService repartidorService;
    private final ClienteService clienteService;

    public UsuarioService(UsuarioRepository usuarioRepository, AdministradorService administradorService,
                          RepartidorService repartidorService, ClienteService clienteService) {
        this.usuarioRepository = usuarioRepository;
        this.administradorService = administradorService;
        this.repartidorService = repartidorService;
        this.clienteService = clienteService;
    }

    public List<UsuarioEntity> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public ZonaUsuarioDTO getZonaDeCliente(int idCliente) {
        return usuarioRepository.obtenerZonaDeCliente(idCliente);
    }

    public ZonaUsuarioDTO verZonaUser(int idCliente) {
        return usuarioRepository.verZonaUser(idCliente);
    }


    public UsuarioEntity getUsuarioById(Integer id) {
        return usuarioRepository.findById(id);
    }

    public UsuarioEntity getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public UsuarioEntity createUsuario(UsuarioEntity usuario) {
        UsuarioEntity newUsuario = usuarioRepository.save(usuario);
        String tipo = usuario.getTipo();
        if (tipo.equals("ADMIN")) {
            administradorService.createAdministrador(new AdministradorEntity(newUsuario.getId(),1 , null));
        } else if (tipo.equals("REPARTIDOR")) {
            repartidorService.createRepartidor(new RepartidorEntity(newUsuario.getId(), null));
        } else if (tipo.equals("CLIENTE")) {
            clienteService.createCliente(new ClienteEntity(newUsuario.getId(), null));
        }
        return newUsuario;
    }

    public UsuarioEntity updateUsuario(UsuarioEntity usuario) {
        usuarioRepository.update(usuario);
        return usuario;
    }

    public void deleteUsuario(Integer id) {
        usuarioRepository.delete(id);
    }

    public long countUsuarios() {
        return usuarioRepository.count();
    }

    public List<ZonaCoberturaDTO> obtenerZonasDeUsuario(int usuarioId) {
        List<ZonaCoberturaDTO> zonas = usuarioRepository.obtenerZonasPorUsuario(usuarioId);
        if (zonas.isEmpty()) {
            throw new IllegalStateException("El usuario no se encuentra en ninguna zona de cobertura.");
        }
        return zonas;
    }
}