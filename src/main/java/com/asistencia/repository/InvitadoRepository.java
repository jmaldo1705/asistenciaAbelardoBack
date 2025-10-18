package com.asistencia.repository;

import com.asistencia.model.Invitado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitadoRepository extends JpaRepository<Invitado, Long> {
    List<Invitado> findByCoordinadorId(Long coordinadorId);
}
