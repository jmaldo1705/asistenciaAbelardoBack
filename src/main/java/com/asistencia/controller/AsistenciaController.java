package com.asistencia.controller;

import com.asistencia.model.Asistencia;
import com.asistencia.service.AsistenciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asistencias")
@CrossOrigin(origins = "http://localhost:4200")
public class AsistenciaController {
    
    @Autowired
    private AsistenciaService asistenciaService;
    
    // Obtener todas las asistencias
    @GetMapping
    public ResponseEntity<List<Asistencia>> obtenerTodas() {
        List<Asistencia> asistencias = asistenciaService.obtenerTodas();
        return ResponseEntity.ok(asistencias);
    }
    
    // Obtener asistencia por ID
    @GetMapping("/{id}")
    public ResponseEntity<Asistencia> obtenerPorId(@PathVariable Long id) {
        return asistenciaService.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Crear nueva asistencia
    @PostMapping
    public ResponseEntity<Asistencia> crear(@Valid @RequestBody Asistencia asistencia) {
        Asistencia nuevaAsistencia = asistenciaService.crear(asistencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaAsistencia);
    }
    
    // Actualizar asistencia
    @PutMapping("/{id}")
    public ResponseEntity<Asistencia> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Asistencia asistencia) {
        try {
            Asistencia asistenciaActualizada = asistenciaService.actualizar(id, asistencia);
            return ResponseEntity.ok(asistenciaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Eliminar asistencia
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        asistenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    // Buscar por nombre
    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<Asistencia>> buscarPorNombre(@PathVariable String nombre) {
        List<Asistencia> asistencias = asistenciaService.buscarPorNombre(nombre);
        return ResponseEntity.ok(asistencias);
    }
    
    // Buscar por estado de presencia
    @GetMapping("/buscar/presente/{presente}")
    public ResponseEntity<List<Asistencia>> buscarPorPresente(@PathVariable Boolean presente) {
        List<Asistencia> asistencias = asistenciaService.buscarPorPresente(presente);
        return ResponseEntity.ok(asistencias);
    }
}
