package com.asistencia.service;

import com.asistencia.model.Coordinador;
import com.asistencia.model.Llamada;
import com.asistencia.repository.CoordinadorRepository;
import com.asistencia.repository.LlamadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LlamadaService {
    
    @Autowired
    private LlamadaRepository llamadaRepository;
    
    @Autowired
    private CoordinadorRepository coordinadorRepository;
    
    @Transactional
    public Llamada registrarLlamada(Long coordinadorId, String observaciones) {
        Coordinador coordinador = coordinadorRepository.findById(coordinadorId)
            .orElseThrow(() -> new RuntimeException("Coordinador no encontrado"));
        
        Llamada llamada = new Llamada();
        llamada.setFecha(LocalDateTime.now());
        llamada.setObservaciones(observaciones);
        llamada.setCoordinador(coordinador);
        
        return llamadaRepository.save(llamada);
    }
    
    public List<Llamada> obtenerLlamadasPorCoordinador(Long coordinadorId) {
        return llamadaRepository.findByCoordinadorIdOrderByFechaDesc(coordinadorId);
    }
    
    public void eliminarLlamada(Long llamadaId) {
        llamadaRepository.deleteById(llamadaId);
    }
}

