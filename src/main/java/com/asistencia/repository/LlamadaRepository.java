package com.asistencia.repository;

import com.asistencia.model.Llamada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LlamadaRepository extends JpaRepository<Llamada, Long> {
    List<Llamada> findByCoordinadorIdOrderByFechaDesc(Long coordinadorId);
}

