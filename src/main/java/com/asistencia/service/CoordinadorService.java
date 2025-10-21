package com.asistencia.service;

import com.asistencia.model.Coordinador;
import com.asistencia.model.EstadoCoordinador;
import com.asistencia.model.Invitado;
import com.asistencia.repository.CoordinadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoordinadorService {
    
    @Autowired
    private CoordinadorRepository coordinadorRepository;
    
    public List<Coordinador> obtenerTodos() {
        return coordinadorRepository.findAll();
    }
    
    public Optional<Coordinador> obtenerPorId(Long id) {
        return coordinadorRepository.findById(id);
    }
    
    public List<Coordinador> buscarPorMunicipio(String municipio) {
        return coordinadorRepository.findByMunicipioContainingIgnoreCase(municipio);
    }
    
    public List<Coordinador> buscarPorEstadoConfirmacion(Boolean confirmado) {
        return coordinadorRepository.findByConfirmado(confirmado);
    }
    
    public Coordinador guardar(Coordinador coordinador) {
        return coordinadorRepository.save(coordinador);
    }
    
    public Coordinador confirmarLlamada(Long id, Integer numeroInvitados, String observaciones) {
        Coordinador coordinador = coordinadorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Coordinador no encontrado"));
        
        coordinador.setConfirmado(true);
        coordinador.setEstado(EstadoCoordinador.CONFIRMADO);
        // Usar la hora de Colombia (America/Bogota)
        coordinador.setFechaLlamada(LocalDateTime.now(ZoneId.of("America/Bogota")));
        coordinador.setNumeroInvitados(numeroInvitados);
        coordinador.setObservaciones(observaciones);
        
        return coordinadorRepository.save(coordinador);
    }

    public Coordinador desmarcarConfirmacion(Long id) {
        Coordinador coordinador = coordinadorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Coordinador no encontrado"));

        coordinador.setConfirmado(false);
        coordinador.setFechaLlamada(null);
        coordinador.setNumeroInvitados(0);
        coordinador.setObservaciones(null);
        coordinador.setEstado(EstadoCoordinador.PENDIENTE); // Resetear a PENDIENTE

        // Eliminar todos los invitados
        coordinador.getInvitados().clear();

        return coordinadorRepository.save(coordinador);
    }

    public Coordinador actualizarEstado(Long id, String estado, String observaciones) {
        Coordinador coordinador = coordinadorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Coordinador no encontrado"));

        try {
            EstadoCoordinador nuevoEstado = EstadoCoordinador.valueOf(estado.toUpperCase());
            coordinador.setEstado(nuevoEstado);

            // Sincronizar con el campo confirmado
            if (nuevoEstado == EstadoCoordinador.CONFIRMADO) {
                coordinador.setConfirmado(true);
                coordinador.setFechaLlamada(LocalDateTime.now(ZoneId.of("America/Bogota")));
            } else {
                coordinador.setConfirmado(false);
                if (nuevoEstado != EstadoCoordinador.PENDIENTE) {
                    coordinador.setFechaLlamada(LocalDateTime.now(ZoneId.of("America/Bogota")));
                }
            }

            if (observaciones != null && !observaciones.trim().isEmpty()) {
                coordinador.setObservaciones(observaciones);
            }

            return coordinadorRepository.save(coordinador);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido: " + estado);
        }
    }

    public Coordinador agregarInvitado(Long coordinadorId, Invitado invitado) {
        Coordinador coordinador = coordinadorRepository.findById(coordinadorId)
                .orElseThrow(() -> new RuntimeException("Coordinador no encontrado"));

        coordinador.addInvitado(invitado);
        return coordinadorRepository.save(coordinador);
    }

    public long contarConfirmados() {
        // Contar confirmados incluyendo los que tienen estado CONFIRMADO y los que tienen confirmado=true sin estado
        long confirmadosConEstado = coordinadorRepository.countByEstado(EstadoCoordinador.CONFIRMADO);
        long confirmadosSinEstado = coordinadorRepository.countConfirmadosSinEstado();
        return confirmadosConEstado + confirmadosSinEstado;
    }

    public long contarPendientes() {
        // Contar pendientes incluyendo los que tienen estado PENDIENTE y los que tienen confirmado=false sin estado
        long pendientesConEstado = coordinadorRepository.countByEstado(EstadoCoordinador.PENDIENTE);
        long pendientesSinEstado = coordinadorRepository.countPendientesSinEstado();
        return pendientesConEstado + pendientesSinEstado;
    }

    public long contarNoAsiste() {
        return coordinadorRepository.countByEstado(EstadoCoordinador.NO_ASISTE);
    }

    public long contarNoContesta() {
        return coordinadorRepository.countByEstado(EstadoCoordinador.NO_CONTESTA);
    }

    public Coordinador eliminarInvitado(Long coordinadorId, Long invitadoId) {
        Coordinador coordinador = coordinadorRepository.findById(coordinadorId)
            .orElseThrow(() -> new RuntimeException("Coordinador no encontrado"));
        
        boolean removed = coordinador.getInvitados().removeIf(invitado -> invitado.getId().equals(invitadoId));
        
        if (!removed) {
            throw new RuntimeException("Invitado no encontrado");
        }
        
        return coordinadorRepository.save(coordinador);
    }
    
    public void eliminar(Long id) {
        coordinadorRepository.deleteById(id);
    }
    
    public long contarTotal() {
        return coordinadorRepository.count();
    }
}
