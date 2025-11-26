package com.asistencia.controller;

import com.asistencia.dto.UsuarioCreateRequest;
import com.asistencia.dto.UsuarioUpdateRequest;
import com.asistencia.model.Usuario;
import com.asistencia.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.obtenerPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<?> obtenerPorUsername(@PathVariable String username) {
        try {
            Usuario usuario = usuarioService.obtenerPorUsername(username);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody UsuarioCreateRequest request) {
        try {
            Usuario nuevoUsuario = usuarioService.crear(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequest request) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizar(id, request);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{usuarioId}/roles")
    public ResponseEntity<?> asignarRoles(
            @PathVariable Long usuarioId,
            @RequestBody Set<Long> rolesIds) {
        try {
            Usuario usuario = usuarioService.asignarRoles(usuarioId, rolesIds);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{usuarioId}/roles/{rolId}")
    public ResponseEntity<?> agregarRol(
            @PathVariable Long usuarioId,
            @PathVariable Long rolId) {
        try {
            Usuario usuario = usuarioService.agregarRol(usuarioId, rolId);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{usuarioId}/roles/{rolId}")
    public ResponseEntity<?> quitarRol(
            @PathVariable Long usuarioId,
            @PathVariable Long rolId) {
        try {
            Usuario usuario = usuarioService.quitarRol(usuarioId, rolId);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/desbloquear")
    public ResponseEntity<?> desbloquearCuenta(@PathVariable Long id) {
        try {
            usuarioService.desbloquearCuenta(id);
            return ResponseEntity.ok(Map.of("mensaje", "Cuenta desbloqueada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/resetear-password")
    public ResponseEntity<?> resetearPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        try {
            String nuevaPassword = payload.get("nuevaPassword");
            if (nuevaPassword == null || nuevaPassword.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "La nueva contraseña es obligatoria"));
            }
            usuarioService.resetearPassword(id, nuevaPassword);
            return ResponseEntity.ok(Map.of("mensaje", "Contraseña reseteada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
