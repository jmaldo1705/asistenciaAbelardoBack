package com.asistencia.repository;

import com.asistencia.model.Coordinador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoordinadorRepository extends JpaRepository<Coordinador, Long> {
    List<Coordinador> findByMunicipioContainingIgnoreCase(String municipio);
    List<Coordinador> findByConfirmado(Boolean confirmado);
    List<Coordinador> findByMunicipioAndConfirmado(String municipio, Boolean confirmado);
}
