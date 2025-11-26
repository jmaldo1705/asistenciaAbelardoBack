package com.asistencia.controller;

import com.asistencia.model.Llamada;
import com.asistencia.service.LlamadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/llamadas")
public class LlamadaController {

    @Autowired
    private LlamadaService llamadaService;

    @PostMapping("/coordinador/{coordinadorId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EDITOR')")
    public ResponseEntity<Llamada> registrarLlamada(
            @PathVariable Long coordinadorId,
            @RequestBody Map<String, Object> datos) {

        String observaciones = (String) datos.get("observaciones");
        Long eventoId = null;
        if (datos.containsKey("eventoId") && datos.get("eventoId") != null) {
            eventoId = Long.valueOf(datos.get("eventoId").toString());
        }

        Llamada llamada = llamadaService.registrarLlamada(coordinadorId, observaciones, eventoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(llamada);
    }

    @GetMapping("/coordinador/{coordinadorId}")
    public ResponseEntity<List<Llamada>> obtenerLlamadasPorCoordinador(@PathVariable Long coordinadorId) {
        List<Llamada> llamadas = llamadaService.obtenerLlamadasPorCoordinador(coordinadorId);
        return ResponseEntity.ok(llamadas);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EDITOR')")
    public ResponseEntity<Void> eliminarLlamada(@PathVariable Long id) {
        llamadaService.eliminarLlamada(id);
        return ResponseEntity.noContent().build();
    }
}
