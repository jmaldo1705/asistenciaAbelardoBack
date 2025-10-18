package com.asistencia.controller;

import com.asistencia.model.Invitado;
import com.asistencia.service.InvitadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invitados")
@CrossOrigin(origins = "http://localhost:4200")
public class InvitadoController {
    
    @Autowired
    private InvitadoService invitadoService;
    
    @GetMapping
    public ResponseEntity<List<Invitado>> obtenerTodos() {
        return ResponseEntity.ok(invitadoService.obtenerTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Invitado> obtenerPorId(@PathVariable Long id) {
        return invitadoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/coordinador/{coordinadorId}")
    public ResponseEntity<List<Invitado>> obtenerPorCoordinador(@PathVariable Long coordinadorId) {
        return ResponseEntity.ok(invitadoService.obtenerPorCoordinador(coordinadorId));
    }
    
    @PostMapping
    public ResponseEntity<Invitado> crear(@Valid @RequestBody Invitado invitado) {
        Invitado nuevoInvitado = invitadoService.guardar(invitado);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInvitado);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Invitado> actualizar(@PathVariable Long id, @Valid @RequestBody Invitado invitado) {
        return invitadoService.obtenerPorId(id)
                .map(invitadoExistente -> {
                    invitado.setId(id);
                    return ResponseEntity.ok(invitadoService.guardar(invitado));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        invitadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
