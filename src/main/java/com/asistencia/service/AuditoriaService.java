package com.asistencia.service;

import com.asistencia.dto.AuditoriaDTO;
import com.asistencia.model.RegistroAuditoria;
import com.asistencia.repository.RegistroAuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditoriaService {
    
    @Autowired
    private RegistroAuditoriaRepository registroAuditoriaRepository;
    
    public Page<AuditoriaDTO> obtenerAuditoria(LocalDateTime fechaInicio, LocalDateTime fechaFin, 
                                                String entidad, Pageable pageable) {
        try {
            List<RegistroAuditoria> registrosPermanentes;
            
            // Obtener registros de la tabla de auditoría permanente
            if (fechaInicio != null && fechaFin != null) {
                if (entidad != null && !entidad.isEmpty() && !entidad.equalsIgnoreCase("all")) {
                    String entidadCapitalizada = entidad.substring(0, 1).toUpperCase() + entidad.substring(1).toLowerCase();
                    registrosPermanentes = registroAuditoriaRepository.findByEntidadAndFechaBetween(
                        entidadCapitalizada, fechaInicio, fechaFin);
                } else {
                    registrosPermanentes = registroAuditoriaRepository.findByFechaBetween(fechaInicio, fechaFin);
                }
            } else {
                if (entidad != null && !entidad.isEmpty() && !entidad.equalsIgnoreCase("all")) {
                    String entidadCapitalizada = entidad.substring(0, 1).toUpperCase() + entidad.substring(1).toLowerCase();
                    registrosPermanentes = registroAuditoriaRepository.findAllByOrderByFechaDesc().stream()
                        .filter(r -> r.getEntidad().equalsIgnoreCase(entidadCapitalizada))
                        .collect(Collectors.toList());
                } else {
                    registrosPermanentes = registroAuditoriaRepository.findAllByOrderByFechaDesc();
                }
            }
            
            // Convertir registros permanentes a DTOs
            List<AuditoriaDTO> auditoria = new ArrayList<>();
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
            
            // Ordenar por fecha descendente (más reciente primero)
            auditoria.sort((a, b) -> b.getFecha().compareTo(a.getFecha()));
            
            // Aplicar paginación manual
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), auditoria.size());
            
            List<AuditoriaDTO> paginatedList = start < auditoria.size() ? auditoria.subList(start, end) : new ArrayList<>();
            
            return new PageImpl<>(paginatedList, pageable, auditoria.size());
        } catch (Exception e) {
            System.err.println("❌ Error en obtenerAuditoria: " + e.getMessage());
            e.printStackTrace();
            // Retornar página vacía en caso de error
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }
}
