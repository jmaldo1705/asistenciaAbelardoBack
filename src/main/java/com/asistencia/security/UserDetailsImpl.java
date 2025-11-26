package com.asistencia.security;

import com.asistencia.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String username;
    private String email;
    private String nombreCompleto;
    
    @JsonIgnore
    private String password;
    
    private Boolean activo;
    private Boolean cuentaBloqueada;
    private Collection<? extends GrantedAuthority> authorities;
    
    public UserDetailsImpl(Long id, String username, String email, String nombreCompleto,
                          String password, Boolean activo, Boolean cuentaBloqueada,
                          Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.password = password;
        this.activo = activo;
        this.cuentaBloqueada = cuentaBloqueada;
        this.authorities = authorities;
    }
    
    public static UserDetailsImpl build(Usuario usuario) {
        List<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                .collect(Collectors.toList());
        
        return new UserDetailsImpl(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNombreCompleto(),
                usuario.getPassword(),
                usuario.getActivo(),
                usuario.getCuentaBloqueada(),
                authorities);
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return !cuentaBloqueada;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return activo;
    }
}
