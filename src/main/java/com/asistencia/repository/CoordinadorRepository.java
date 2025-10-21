package com.asistencia.repository;

import com.asistencia.model.Coordinador;
import com.asistencia.model.EstadoCoordinador;
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

    // Métodos para los nuevos estados
    List<Coordinador> findByEstado(EstadoCoordinador estado);

    @Query("SELECT COUNT(c) FROM Coordinador c WHERE c.estado = :estado")
    Long countByEstado(@Param("estado") EstadoCoordinador estado);

    @Query("SELECT COUNT(c) FROM Coordinador c WHERE c.estado IS NULL AND c.confirmado = true")
    Long countConfirmadosSinEstado();

    @Query("SELECT COUNT(c) FROM Coordinador c WHERE c.estado IS NULL AND c.confirmado = false")
    Long countPendientesSinEstado();
}
