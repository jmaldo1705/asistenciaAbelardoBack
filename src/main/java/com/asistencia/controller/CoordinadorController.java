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
public class CoordinadorController {

    @Autowired
    private CoordinadorService coordinadorService;

    @GetMapping
    public ResponseEntity<List<Coordinador>> obtenerTodos() {
        try {
            List<Coordinador> coordinadores = coordinadorService.obtenerTodos();
            return ResponseEntity.ok(coordinadores);
        } catch (Exception e) {
            System.err.println("❌ Error al obtener coordinadores: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
                    coordinadorExistente.setMunicipio(coordinador.getMunicipio());
                    coordinadorExistente.setSector(coordinador.getSector());
                    coordinadorExistente.setNombreCompleto(coordinador.getNombreCompleto());
                    coordinadorExistente.setCelular(coordinador.getCelular());
                    coordinadorExistente.setEmail(coordinador.getEmail());
                    coordinadorExistente.setLatitud(coordinador.getLatitud());
                    coordinadorExistente.setLongitud(coordinador.getLongitud());
                    // Preservar campos no editables: fechaLlamada, observaciones, confirmado,
                    // numeroInvitados, llamadas
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
                "total", coordinadorService.contarTotal());
        return ResponseEntity.ok(estadisticas);
    }

    @PostMapping("/{coordinadorId}/eventos/{eventoId}")
    public ResponseEntity<Void> asignarEvento(@PathVariable Long coordinadorId, @PathVariable Long eventoId) {
        coordinadorService.asignarEvento(coordinadorId, eventoId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{coordinadorId}/eventos/{eventoId}")
    public ResponseEntity<Void> desasignarEvento(@PathVariable Long coordinadorId, @PathVariable Long eventoId) {
        coordinadorService.desasignarEvento(coordinadorId, eventoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/por-evento-llamadas/{eventoId}")
    public ResponseEntity<List<Coordinador>> obtenerPorEventoEnLlamadas(@PathVariable Long eventoId) {
        try {
            List<Coordinador> coordinadores = coordinadorService.obtenerPorEventoEnLlamadas(eventoId);
            return ResponseEntity.ok(coordinadores);
        } catch (Exception e) {
            System.err.println("❌ Error al obtener coordinadores por evento en llamadas: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
