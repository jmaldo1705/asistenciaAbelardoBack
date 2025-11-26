package com.asistencia.controller;

import com.asistencia.dto.CambiarPasswordRequest;
import com.asistencia.dto.LoginRequest;
import com.asistencia.dto.LoginResponse;
import com.asistencia.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Endpoint temporal para generar hash - ELIMINAR EN PRODUCCIÓN
    @GetMapping("/generate-hash")
    public ResponseEntity<?> generateHash(@RequestParam String password) {
        String hash = passwordEncoder.encode(password);
        return ResponseEntity.ok(Map.of(
            "password", password,
            "hash", hash
        ));
    }
    
    // Endpoint temporal para resetear password del admin - ELIMINAR EN PRODUCCIÓN
    @GetMapping("/reset-admin-password")
    public ResponseEntity<?> resetAdminPassword() {
        try {
            String newPassword = "Admin2025$Temp";
            String hash = passwordEncoder.encode(newPassword);
            return ResponseEntity.ok(Map.of(
                "mensaje", "Hash generado correctamente",
                "password", newPassword,
                "hash", hash,
                "instrucciones", "Actualiza manualmente en la base de datos: UPDATE usuarios SET password = '" + hash + "', debe_cambiar_password = true WHERE username = 'admin';"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/cambiar-password")
    public ResponseEntity<?> cambiarPassword(@Valid @RequestBody CambiarPasswordRequest request) {
        try {
            authService.cambiarPassword(request);
            return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // El logout se maneja en el frontend eliminando el token
        return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada exitosamente"));
    }
}
