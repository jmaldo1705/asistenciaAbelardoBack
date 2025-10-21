package com.asistencia.controller;

import com.asistencia.model.Coordinador;
import com.asistencia.model.Invitado;
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
                    coordinador.setId(id);
                    return ResponseEntity.ok(coordinadorService.guardar(coordinador));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Coordinador> confirmarLlamada(
            @PathVariable Long id,
            @RequestBody Map<String, Object> datos) {
        
        Integer numeroInvitados = (Integer) datos.get("numeroInvitados");
        String observaciones = (String) datos.get("observaciones");
        
        Coordinador coordinador = coordinadorService.confirmarLlamada(id, numeroInvitados, observaciones);
        return ResponseEntity.ok(coordinador);
    }
    
    @PutMapping("/{id}/desmarcar")
    public ResponseEntity<Coordinador> desmarcarConfirmacion(@PathVariable Long id) {
        Coordinador coordinador = coordinadorService.desmarcarConfirmacion(id);
        return ResponseEntity.ok(coordinador);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Coordinador> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, Object> datos) {

        String estado = (String) datos.get("estado");
        String observaciones = (String) datos.get("observaciones");

        Coordinador coordinador = coordinadorService.actualizarEstado(id, estado, observaciones);
        return ResponseEntity.ok(coordinador);
    }
    
    @PostMapping("/{id}/invitados")
    public ResponseEntity<Coordinador> agregarInvitado(
            @PathVariable Long id,
            @Valid @RequestBody Invitado invitado) {
        
        Coordinador coordinador = coordinadorService.agregarInvitado(id, invitado);
        return ResponseEntity.ok(coordinador);
    }
    
    @DeleteMapping("/{coordinadorId}/invitados/{invitadoId}")
    public ResponseEntity<Coordinador> eliminarInvitado(
            @PathVariable Long coordinadorId,
            @PathVariable Long invitadoId) {

        Coordinador coordinador = coordinadorService.eliminarInvitado(coordinadorId, invitadoId);
        return ResponseEntity.ok(coordinador);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        coordinadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticas() {
        Map<String, Long> estadisticas = Map.of(
            "total", coordinadorService.contarTotal(),
            "confirmados", coordinadorService.contarConfirmados(),
            "pendientes", coordinadorService.contarPendientes(),
            "noAsiste", coordinadorService.contarNoAsiste(),
            "noContesta", coordinadorService.contarNoContesta()
        );
        return ResponseEntity.ok(estadisticas);
    }
}
