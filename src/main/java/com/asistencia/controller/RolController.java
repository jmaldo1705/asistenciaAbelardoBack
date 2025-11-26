package com.asistencia.controller;

import com.asistencia.model.Rol;
import com.asistencia.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    
    @Autowired
    private RolRepository rolRepository;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Rol>> obtenerTodos() {
        return ResponseEntity.ok(rolRepository.findAll());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return rolRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
