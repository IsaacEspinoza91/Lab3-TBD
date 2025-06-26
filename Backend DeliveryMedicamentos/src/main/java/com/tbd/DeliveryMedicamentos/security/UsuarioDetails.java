package com.tbd.DeliveryMedicamentos.security;

import com.tbd.DeliveryMedicamentos.entities.UsuarioEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UsuarioDetails implements UserDetails {

    private final UsuarioEntity usuario;

    public UsuarioDetails(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convierte el tipo de usuario a un rol (ej. "CLIENTE" -> "ROLE_CLIENTE")
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getTipo()));
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail(); // Usamos el email como username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Puedes implementar lógica de expiración si es necesario
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Puedes implementar lógica de bloqueo si es necesario
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Puedes implementar lógica de expiración de credenciales
    }

    @Override
    public boolean isEnabled() {
        return true; // Puedes implementar lógica de habilitación/deshabilitación
    }
}