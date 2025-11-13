package com.asistencia.controller;

import com.asistencia.model.Coordinador;
import com.asistencia.service.CoordinadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coordinadores")
@CrossOrigin(origins = "http://localhost:4200")
public class CoordinadorController {
    
    @Autowired
    private CoordinadorService coordinadorService;
    
    @GetMapping
    public ResponseEntity<List<Coordinador>> obtenerTodos() {
        return ResponseEntity.ok(coordinadorService.obtenerTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Coordinador> obtenerPorId(@PathVariable Long id) {
        return coordinadorService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Coordinador>> buscarPorMunicipio(@RequestParam String municipio) {
        return ResponseEntity.ok(coordinadorService.buscarPorMunicipio(municipio));
    }
    
    @GetMapping("/confirmados")
    public ResponseEntity<List<Coordinador>> obtenerConfirmados(@RequestParam Boolean confirmado) {
        return ResponseEntity.ok(coordinadorService.buscarPorEstadoConfirmacion(confirmado));
    }
    
    @PostMapping
    public ResponseEntity<Coordinador> crear(@Valid @RequestBody Coordinador coordinador) {
        Coordinador nuevoCoordinador = coordinadorService.guardar(coordinador);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCoordinador);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Coordinador> actualizar(@PathVariable Long id, @Valid @RequestBody Coordinador coordinador) {
        return coordinadorService.obtenerPorId(id)
                .map(coordinadorExistente -> {
                    // Solo actualizar campos editables
                    coordinadorExistente.setCiudad(coordinador.getCiudad());
                    coordinadorExistente.setMunicipio(coordinador.getMunicipio());
                    coordinadorExistente.setNombreCompleto(coordinador.getNombreCompleto());
                    coordinadorExistente.setCelular(coordinador.getCelular());
                    coordinadorExistente.setEmail(coordinador.getEmail());
                    // Preservar campos no editables: fechaLlamada, observaciones, confirmado, numeroInvitados, llamadas
                    return ResponseEntity.ok(coordinadorService.guardar(coordinadorExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        coordinadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticas() {
        Map<String, Long> estadisticas = Map.of(
            "total", coordinadorService.contarTotal()
        );
        return ResponseEntity.ok(estadisticas);
    }
}
