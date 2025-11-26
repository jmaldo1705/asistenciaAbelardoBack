package com.asistencia.service;

import com.asistencia.dto.UsuarioCreateRequest;
import com.asistencia.dto.UsuarioUpdateRequest;
import com.asistencia.model.Rol;
import com.asistencia.model.Usuario;
import com.asistencia.repository.RolRepository;
import com.asistencia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }
    
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }
    
    public Usuario obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
    }
    
    @Transactional
    public Usuario crear(UsuarioCreateRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setEmail(request.getEmail());
        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setActivo(true);
        usuario.setDebeCambiarPassword(true);
        usuario.setIntentosFallidos(0);
        usuario.setCuentaBloqueada(false);
        
        // Asignar roles
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            Set<Rol> roles = new HashSet<>();
            for (Long rolId : request.getRoles()) {
                Rol rol = rolRepository.findById(rolId)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + rolId));
                roles.add(rol);
            }
            usuario.setRoles(roles);
        }
        
        return usuarioRepository.save(usuario);
    }
    
    @Transactional
    public Usuario actualizar(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = obtenerPorId(id);
        
        // Verificar username único (si cambió)
        if (!usuario.getUsername().equals(request.getUsername()) &&
            usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        
        // Verificar email único (si cambió)
        if (!usuario.getEmail().equals(request.getEmail()) &&
            usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setNombreCompleto(request.getNombreCompleto());
        
        if (request.getActivo() != null) {
            usuario.setActivo(request.getActivo());
        }
        
        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        
        // Solo actualizar password si se proporciona uno nuevo
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
            usuario.setDebeCambiarPassword(true);
        }
        
        return usuarioRepository.save(usuario);
    }
    
    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuarioRepository.delete(usuario);
    }
    
    @Transactional
    public Usuario asignarRoles(Long usuarioId, Set<Long> rolesIds) {
        Usuario usuario = obtenerPorId(usuarioId);
        Set<Rol> roles = new HashSet<>();
        
        for (Long rolId : rolesIds) {
            Rol rol = rolRepository.findById(rolId)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + rolId));
            roles.add(rol);
        }
        
        usuario.setRoles(roles);
        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }
    
    @Transactional
    public Usuario agregarRol(Long usuarioId, Long rolId) {
        Usuario usuario = obtenerPorId(usuarioId);
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + rolId));
        
        usuario.addRol(rol);
        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }
    
    @Transactional
    public Usuario quitarRol(Long usuarioId, Long rolId) {
        Usuario usuario = obtenerPorId(usuarioId);
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + rolId));
        
        usuario.removeRol(rol);
        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }
    
    @Transactional
    public void desbloquearCuenta(Long usuarioId) {
        Usuario usuario = obtenerPorId(usuarioId);
        usuario.resetearIntentosFallidos();
        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }
    
    @Transactional
    public void resetearPassword(Long usuarioId, String nuevaPassword) {
        Usuario usuario = obtenerPorId(usuarioId);
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuario.setDebeCambiarPassword(true);
        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }
}
