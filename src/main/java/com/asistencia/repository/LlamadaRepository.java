package com.asistencia.repository;

import com.asistencia.model.Llamada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LlamadaRepository extends JpaRepository<Llamada, Long> {
    List<Llamada> findByCoordinadorIdOrderByFechaDesc(Long coordinadorId);
    
    @Query("SELECT l FROM Llamada l LEFT JOIN FETCH l.evento WHERE l.coordinador.id = :coordinadorId ORDER BY l.fecha DESC")
    List<Llamada> findByCoordinadorIdWithEventoOrderByFechaDesc(@Param("coordinadorId") Long coordinadorId);
}

