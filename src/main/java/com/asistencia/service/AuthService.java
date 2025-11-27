package com.asistencia.service;

import com.asistencia.dto.CambiarPasswordRequest;
import com.asistencia.dto.LoginRequest;
import com.asistencia.dto.LoginResponse;
import com.asistencia.model.Usuario;
import com.asistencia.repository.UsuarioRepository;
import com.asistencia.security.JwtTokenProvider;
import com.asistencia.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (usuario.getCuentaBloqueada()) {
            throw new RuntimeException("Cuenta bloqueada. Contacte al administrador");
        }
        
        if (!usuario.getActivo()) {
            throw new RuntimeException("Cuenta inactiva. Contacte al administrador");
        }
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Resetear intentos fallidos y actualizar último login
            usuario.resetearIntentosFallidos();
            usuario.setUltimoLogin(LocalDateTime.now());
            usuarioRepository.save(usuario);
            
            String jwt = tokenProvider.generateToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority().replace("ROLE_", ""))
                    .collect(Collectors.toList());
            
            return new LoginResponse(
                    jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    userDetails.getNombreCompleto(),
                    roles,
                    usuario.getDebeCambiarPassword()
            );
        } catch (Exception e) {
            // Incrementar intentos fallidos
            usuario.incrementarIntentosFallidos();
            usuarioRepository.save(usuario);
            
            if (usuario.getCuentaBloqueada()) {
                throw new RuntimeException("Cuenta bloqueada después de múltiples intentos fallidos");
            }
            
            throw new RuntimeException("Credenciales inválidas");
        }
    }
    
    @Transactional
    public void cambiarPassword(CambiarPasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        Usuario usuario = usuarioRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Verificar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(request.getPasswordActual(), usuario.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }
        
        // Verificar que las contraseñas nuevas coincidan
        if (!request.getPasswordNueva().equals(request.getPasswordConfirmacion())) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }
        
        // Verificar que la nueva contraseña no sea igual a la actual
        if (request.getPasswordNueva().equals(request.getPasswordActual())) {
            throw new RuntimeException("La nueva contraseña debe ser diferente a la actual");
        }
        
        // Actualizar contraseña
        usuario.setPassword(passwordEncoder.encode(request.getPasswordNueva()));
        usuario.setDebeCambiarPassword(false);
        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }
}
