package com.asistencia.repository;

import com.asistencia.model.RegistroAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistroAuditoriaRepository extends JpaRepository<RegistroAuditoria, Long> {
    
    List<RegistroAuditoria> findByEntidad(String entidad);
    
    List<RegistroAuditoria> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<RegistroAuditoria> findByEntidadAndFechaBetween(String entidad, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<RegistroAuditoria> findAllByOrderByFechaDesc();
}
