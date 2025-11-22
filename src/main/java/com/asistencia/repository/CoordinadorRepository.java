package com.asistencia.repository;

import com.asistencia.model.Coordinador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoordinadorRepository extends JpaRepository<Coordinador, Long> {
    List<Coordinador> findByMunicipioContainingIgnoreCase(String municipio);
    List<Coordinador> findByConfirmado(Boolean confirmado);
    List<Coordinador> findByMunicipioAndConfirmado(String municipio, Boolean confirmado);
    
    // Métodos para contar por estado de confirmación
    Long countByConfirmado(Boolean confirmado);
    
    @Query("SELECT DISTINCT c FROM Coordinador c JOIN c.llamadas l WHERE l.evento.id = :eventoId")
    List<Coordinador> findByLlamadasEventoId(@Param("eventoId") Long eventoId);
}
