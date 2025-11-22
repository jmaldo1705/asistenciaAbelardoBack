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

    @Autowired
    private com.asistencia.repository.EventoRepository eventoRepository;

    @Transactional
    public Llamada registrarLlamada(Long coordinadorId, String observaciones, Long eventoId) {
        Coordinador coordinador = coordinadorRepository.findById(coordinadorId)
                .orElseThrow(() -> new RuntimeException("Coordinador no encontrado"));

        Llamada llamada = new Llamada();
        llamada.setFecha(LocalDateTime.now());
        llamada.setObservaciones(observaciones);
        llamada.setCoordinador(coordinador);

        if (eventoId != null) {
            com.asistencia.model.Evento evento = eventoRepository.findById(eventoId)
                    .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
            llamada.setEvento(evento);
        }

        Llamada guardada = llamadaRepository.save(llamada);

        // Actualizar la fecha de última llamada del coordinador
        coordinador.setFechaLlamada(guardada.getFecha());
        coordinadorRepository.save(coordinador);

        return guardada;
    }

    public List<Llamada> obtenerLlamadasPorCoordinador(Long coordinadorId) {
        return llamadaRepository.findByCoordinadorIdWithEventoOrderByFechaDesc(coordinadorId);
    }

    public void eliminarLlamada(Long llamadaId) {
        llamadaRepository.deleteById(llamadaId);
    }
}
