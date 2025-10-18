package com.asistencia.service;

import com.asistencia.model.Invitado;
import com.asistencia.repository.InvitadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InvitadoService {
    
    @Autowired
    private InvitadoRepository invitadoRepository;
    
    public List<Invitado> obtenerTodos() {
        return invitadoRepository.findAll();
    }
    
    public Optional<Invitado> obtenerPorId(Long id) {
        return invitadoRepository.findById(id);
    }
    
    public List<Invitado> obtenerPorCoordinador(Long coordinadorId) {
        return invitadoRepository.findByCoordinadorId(coordinadorId);
    }
    
    public Invitado guardar(Invitado invitado) {
        return invitadoRepository.save(invitado);
    }
    
    public void eliminar(Long id) {
        invitadoRepository.deleteById(id);
    }
}
