package com.asistencia.service;

import com.asistencia.dto.AuditoriaDTO;
import com.asistencia.model.*;
import com.asistencia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditoriaService {
    
    @Autowired
    private CoordinadorRepository coordinadorRepository;
    
    @Autowired
    private EventoRepository eventoRepository;
    
    @Autowired
    private LlamadaRepository llamadaRepository;
    
    @Autowired
    private AsistenciaRepository asistenciaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RegistroAuditoriaRepository registroAuditoriaRepository;
    
    public Page<AuditoriaDTO> obtenerAuditoria(LocalDateTime fechaInicio, LocalDateTime fechaFin, 
                                                String entidad, Pageable pageable) {
        List<AuditoriaDTO> auditoria = new ArrayList<>();
        
        // Primero, obtener registros de la tabla de auditoría permanente (incluye eliminaciones)
        List<RegistroAuditoria> registrosPermanentes;
        if (fechaInicio != null && fechaFin != null) {
            if (entidad != null && !entidad.isEmpty() && !entidad.equalsIgnoreCase("all")) {
                String entidadCapitalizada = entidad.substring(0, 1).toUpperCase() + entidad.substring(1).toLowerCase();
                registrosPermanentes = registroAuditoriaRepository.findByEntidadAndFechaBetween(
                    entidadCapitalizada, fechaInicio, fechaFin);
            } else {
                registrosPermanentes = registroAuditoriaRepository.findByFechaBetween(fechaInicio, fechaFin);
            }
        } else {
            registrosPermanentes = registroAuditoriaRepository.findAllByOrderByFechaDesc();
            if (entidad != null && !entidad.isEmpty() && !entidad.equalsIgnoreCase("all")) {
                String entidadCapitalizada = entidad.substring(0, 1).toUpperCase() + entidad.substring(1).toLowerCase();
                registrosPermanentes = registrosPermanentes.stream()
                    .filter(r -> r.getEntidad().equalsIgnoreCase(entidadCapitalizada))
                    .collect(Collectors.toList());
            }
        }
        
        // Convertir registros permanentes a DTOs
        for (RegistroAuditoria reg : registrosPermanentes) {
            auditoria.add(new AuditoriaDTO(
                reg.getId(),
                reg.getEntidad(),
                reg.getEntidadId(),
                reg.getAccion(),
                reg.getUsuario(),
                reg.getFecha(),
                reg.getDetalle()
            ));
        }
        
        // Luego, obtener registros de creación y modificación de las entidades
        if (entidad == null || entidad.isEmpty() || entidad.equalsIgnoreCase("all")) {
            auditoria.addAll(obtenerAuditoriaCoordinadores(fechaInicio, fechaFin));
            auditoria.addAll(obtenerAuditoriaEventos(fechaInicio, fechaFin));
            auditoria.addAll(obtenerAuditoriaLlamadas(fechaInicio, fechaFin));
            auditoria.addAll(obtenerAuditoriaAsistencias(fechaInicio, fechaFin));
        } else {
            switch (entidad.toLowerCase()) {
                case "coordinador":
                    auditoria.addAll(obtenerAuditoriaCoordinadores(fechaInicio, fechaFin));
                    break;
                case "evento":
                    auditoria.addAll(obtenerAuditoriaEventos(fechaInicio, fechaFin));
                    break;
                case "llamada":
                    auditoria.addAll(obtenerAuditoriaLlamadas(fechaInicio, fechaFin));
                    break;
                case "asistencia":
                    auditoria.addAll(obtenerAuditoriaAsistencias(fechaInicio, fechaFin));
                    break;
            }
        }
        
        // Ordenar por fecha descendente (más reciente primero)
        auditoria.sort((a, b) -> b.getFecha().compareTo(a.getFecha()));
        
        // Aplicar paginación manual
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), auditoria.size());
        
        List<AuditoriaDTO> paginatedList = start < auditoria.size() ? auditoria.subList(start, end) : new ArrayList<>();
        
        return new PageImpl<>(paginatedList, pageable, auditoria.size());
    }
    
    private List<AuditoriaDTO> obtenerAuditoriaCoordinadores(LocalDateTime fechaInicio, 
                                                             LocalDateTime fechaFin) {
        List<AuditoriaDTO> resultado = new ArrayList<>();
        List<Coordinador> coordinadores = coordinadorRepository.findAll();
        
        for (Coordinador c : coordinadores) {
            // Creación
            if (c.getCreadoEn() != null && 
                (fechaInicio == null || c.getCreadoEn().isAfter(fechaInicio)) &&
                (fechaFin == null || c.getCreadoEn().isBefore(fechaFin))) {
                
                String usuario = c.getCreadoPorId() != null ? 
                    obtenerNombreUsuario(c.getCreadoPorId()) : "Sistema";
                
                resultado.add(new AuditoriaDTO(
                    c.getId(),
                    "Coordinador",
                    c.getId(),
                    "CREACIÓN",
                    usuario,
                    c.getCreadoEn(),
                    "Coordinador: " + c.getNombreCompleto() + " - " + c.getMunicipio()
                ));
            }
            
            // Modificación
            if (c.getModificadoEn() != null && 
                (fechaInicio == null || c.getModificadoEn().isAfter(fechaInicio)) &&
                (fechaFin == null || c.getModificadoEn().isBefore(fechaFin))) {
                
                String usuario = c.getModificadoPorId() != null ? 
                    obtenerNombreUsuario(c.getModificadoPorId()) : "Sistema";
                
                resultado.add(new AuditoriaDTO(
                    c.getId(),
                    "Coordinador",
                    c.getId(),
                    "MODIFICACIÓN",
                    usuario,
                    c.getModificadoEn(),
                    "Coordinador: " + c.getNombreCompleto() + " - " + c.getMunicipio()
                ));
            }
        }
        
        return resultado;
    }
    
    private List<AuditoriaDTO> obtenerAuditoriaEventos(LocalDateTime fechaInicio, 
                                                       LocalDateTime fechaFin) {
        List<AuditoriaDTO> resultado = new ArrayList<>();
        List<Evento> eventos = eventoRepository.findAll();
        
        for (Evento e : eventos) {
            // Creación
            if (e.getCreadoEn() != null && 
                (fechaInicio == null || e.getCreadoEn().isAfter(fechaInicio)) &&
                (fechaFin == null || e.getCreadoEn().isBefore(fechaFin))) {
                
                String usuario = e.getCreadoPorId() != null ? 
                    obtenerNombreUsuario(e.getCreadoPorId()) : "Sistema";
                
                resultado.add(new AuditoriaDTO(
                    e.getId(),
                    "Evento",
                    e.getId(),
                    "CREACIÓN",
                    usuario,
                    e.getCreadoEn(),
                    "Evento: " + e.getNombre() + " - " + e.getFecha()
                ));
            }
            
            // Modificación
            if (e.getModificadoEn() != null && 
                (fechaInicio == null || e.getModificadoEn().isAfter(fechaInicio)) &&
                (fechaFin == null || e.getModificadoEn().isBefore(fechaFin))) {
                
                String usuario = e.getModificadoPorId() != null ? 
                    obtenerNombreUsuario(e.getModificadoPorId()) : "Sistema";
                
                resultado.add(new AuditoriaDTO(
                    e.getId(),
                    "Evento",
                    e.getId(),
                    "MODIFICACIÓN",
                    usuario,
                    e.getModificadoEn(),
                    "Evento: " + e.getNombre() + " - " + e.getFecha()
                ));
            }
        }
        
        return resultado;
    }
    
    private List<AuditoriaDTO> obtenerAuditoriaLlamadas(LocalDateTime fechaInicio, 
                                                        LocalDateTime fechaFin) {
        List<AuditoriaDTO> resultado = new ArrayList<>();
        List<Llamada> llamadas = llamadaRepository.findAll();
        
        for (Llamada l : llamadas) {
            // Creación
            if (l.getCreadoEn() != null && 
                (fechaInicio == null || l.getCreadoEn().isAfter(fechaInicio)) &&
                (fechaFin == null || l.getCreadoEn().isBefore(fechaFin))) {
                
                String usuario = l.getCreadoPorId() != null ? 
                    obtenerNombreUsuario(l.getCreadoPorId()) : "Sistema";
                
                String coordinadorNombre = l.getCoordinador() != null ? 
                    l.getCoordinador().getNombreCompleto() : "N/A";
                
                resultado.add(new AuditoriaDTO(
                    l.getId(),
                    "Llamada",
                    l.getId(),
                    "CREACIÓN",
                    usuario,
                    l.getCreadoEn(),
                    "Llamada a: " + coordinadorNombre + " - " + l.getFecha()
                ));
            }
        }
        
        return resultado;
    }
    
    private List<AuditoriaDTO> obtenerAuditoriaAsistencias(LocalDateTime fechaInicio, 
                                                           LocalDateTime fechaFin) {
        List<AuditoriaDTO> resultado = new ArrayList<>();
        List<Asistencia> asistencias = asistenciaRepository.findAll();
        
        for (Asistencia a : asistencias) {
            // Creación
            if (a.getCreadoEn() != null && 
                (fechaInicio == null || a.getCreadoEn().isAfter(fechaInicio)) &&
                (fechaFin == null || a.getCreadoEn().isBefore(fechaFin))) {
                
                String usuario = a.getCreadoPorId() != null ? 
                    obtenerNombreUsuario(a.getCreadoPorId()) : "Sistema";
                
                String detalle = "Asistencia registrada: " + a.getNombre() + " " + a.getApellido() + 
                                " (" + a.getEmail() + ")";
                
                resultado.add(new AuditoriaDTO(
                    a.getId(),
                    "Asistencia",
                    a.getId(),
                    "CREACIÓN",
                    usuario,
                    a.getCreadoEn(),
                    detalle
                ));
            }
            
            // Modificación
            if (a.getModificadoEn() != null && 
                (fechaInicio == null || a.getModificadoEn().isAfter(fechaInicio)) &&
                (fechaFin == null || a.getModificadoEn().isBefore(fechaFin))) {
                
                String usuario = a.getModificadoPorId() != null ? 
                    obtenerNombreUsuario(a.getModificadoPorId()) : "Sistema";
                
                String detalle = "Asistencia actualizada: " + a.getNombre() + " " + a.getApellido() + 
                                " (" + a.getEmail() + ")";
                
                resultado.add(new AuditoriaDTO(
                    a.getId(),
                    "Asistencia",
                    a.getId(),
                    "MODIFICACIÓN",
                    usuario,
                    a.getModificadoEn(),
                    detalle
                ));
            }
        }
        
        return resultado;
    }
    
    private String obtenerNombreUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
            .map(u -> u.getNombreCompleto() + " (" + u.getUsername() + ")")
            .orElse("Usuario desconocido");
    }
}
