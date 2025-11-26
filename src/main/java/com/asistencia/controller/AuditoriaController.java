package com.asistencia.controller;

import com.asistencia.dto.AuditoriaDTO;
import com.asistencia.service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@CrossOrigin(origins = "*")
public class AuditoriaController {
    
    @Autowired
    private AuditoriaService auditoriaService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Page<AuditoriaDTO>> obtenerAuditoria(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(required = false) String entidad,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "50") Integer size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fecha"));
        Page<AuditoriaDTO> auditoria = auditoriaService.obtenerAuditoria(fechaInicio, fechaFin, entidad, pageable);
        return ResponseEntity.ok(auditoria);
    }
}
