package com.asistencia.service;

import com.asistencia.model.Evento;
import com.asistencia.model.RegistroAuditoria;
import com.asistencia.repository.EventoRepository;
import com.asistencia.repository.RegistroAuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;
    
    @Autowired
    private RegistroAuditoriaRepository registroAuditoriaRepository;

    public List<Evento> obtenerTodos() {
        return eventoRepository.findAll();
    }

    public Optional<Evento> obtenerPorId(Long id) {
        return eventoRepository.findById(id);
    }

    public Evento crear(Evento evento) {
        Evento guardado = eventoRepository.save(evento);
        
        // Registrar creación en auditoría
        String usuario = "Sistema";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            usuario = auth.getName();
        }
        
        String detalle = "Evento: " + guardado.getNombre() + " - " + guardado.getFecha();
        
        RegistroAuditoria registro = new RegistroAuditoria(
            "Evento",
            guardado.getId(),
            "CREACIÓN",
            usuario,
            detalle
        );
        
        registroAuditoriaRepository.save(registro);
        return guardado;
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
