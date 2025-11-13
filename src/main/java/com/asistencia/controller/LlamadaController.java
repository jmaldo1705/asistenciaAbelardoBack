package com.asistencia.controller;

import com.asistencia.model.Llamada;
import com.asistencia.service.LlamadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/llamadas")
public class LlamadaController {
    
    @Autowired
    private LlamadaService llamadaService;
    
    @PostMapping("/coordinador/{coordinadorId}")
    public ResponseEntity<Llamada> registrarLlamada(
            @PathVariable Long coordinadorId,
            @RequestBody Map<String, String> datos) {
        
        String observaciones = datos.get("observaciones");
        Llamada llamada = llamadaService.registrarLlamada(coordinadorId, observaciones);
        return ResponseEntity.status(HttpStatus.CREATED).body(llamada);
    }
    
    @GetMapping("/coordinador/{coordinadorId}")
    public ResponseEntity<List<Llamada>> obtenerLlamadasPorCoordinador(@PathVariable Long coordinadorId) {
        List<Llamada> llamadas = llamadaService.obtenerLlamadasPorCoordinador(coordinadorId);
        return ResponseEntity.ok(llamadas);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLlamada(@PathVariable Long id) {
        llamadaService.eliminarLlamada(id);
        return ResponseEntity.noContent().build();
    }
}

