package com.asistencia.repository;

import com.asistencia.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    
    // Buscar asistencias por nombre
    List<Asistencia> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar asistencias por apellido
    List<Asistencia> findByApellidoContainingIgnoreCase(String apellido);
    
    // Buscar asistencias por email
    List<Asistencia> findByEmail(String email);
    
    // Buscar asistencias por estado (presente/ausente)
    List<Asistencia> findByPresente(Boolean presente);
    
    // Buscar asistencias en un rango de fechas
    List<Asistencia> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // Ordenar todas las asistencias por fecha descendente
    List<Asistencia> findAllByOrderByFechaHoraDesc();
}
