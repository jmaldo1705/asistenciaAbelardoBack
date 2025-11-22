package com.asistencia.service;

import com.asistencia.model.Evento;
import com.asistencia.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public List<Evento> obtenerTodos() {
        return eventoRepository.findAll();
    }

    public Optional<Evento> obtenerPorId(Long id) {
        return eventoRepository.findById(id);
    }

    public Evento crear(Evento evento) {
        return eventoRepository.save(evento);
    }

    public Evento actualizar(Long id, Evento eventoDetalles) {
        return eventoRepository.findById(id)
                .map(evento -> {
                    evento.setNombre(eventoDetalles.getNombre());
                    evento.setLugar(eventoDetalles.getLugar());
                    evento.setFecha(eventoDetalles.getFecha());
                    evento.setObservaciones(eventoDetalles.getObservaciones());
                    return eventoRepository.save(evento);
                })
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con id " + id));
    }

    public void eliminar(Long id) {
        eventoRepository.deleteById(id);
    }
}
