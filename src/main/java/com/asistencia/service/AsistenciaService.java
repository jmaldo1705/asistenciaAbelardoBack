package com.asistencia.service;

import com.asistencia.model.Asistencia;
import com.asistencia.model.RegistroAuditoria;
import com.asistencia.repository.AsistenciaRepository;
import com.asistencia.repository.RegistroAuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AsistenciaService {
    
    @Autowired
    private AsistenciaRepository asistenciaRepository;
    
    @Autowired
    private RegistroAuditoriaRepository registroAuditoriaRepository;
    
    // Obtener todas las asistencias
    public List<Asistencia> obtenerTodas() {
        return asistenciaRepository.findAllByOrderByFechaHoraDesc();
    }
    
    // Obtener asistencia por ID
    public Optional<Asistencia> obtenerPorId(Long id) {
        return asistenciaRepository.findById(id);
    }
    
    // Crear nueva asistencia
    public Asistencia crear(Asistencia asistencia) {
        asistencia.setFechaHora(LocalDateTime.now());
        Asistencia guardada = asistenciaRepository.save(asistencia);
        
        // Registrar creación en auditoría
        String usuario = "Sistema";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            usuario = auth.getName();
        }
        
        String detalle = "Asistencia registrada: " + guardada.getNombre() + " " + 
                        guardada.getApellido() + " (" + guardada.getEmail() + ")";
        
        RegistroAuditoria registro = new RegistroAuditoria(
            "Asistencia",
            guardada.getId(),
            "CREACIÓN",
            usuario,
            detalle
        );
        
        registroAuditoriaRepository.save(registro);
        return guardada;
    }
    
    // Actualizar asistencia
    public Asistencia actualizar(Long id, Asistencia asistenciaActualizada) {
        return asistenciaRepository.findById(id)
            .map(asistencia -> {
                asistencia.setNombre(asistenciaActualizada.getNombre());
                asistencia.setApellido(asistenciaActualizada.getApellido());
                asistencia.setEmail(asistenciaActualizada.getEmail());
                asistencia.setObservaciones(asistenciaActualizada.getObservaciones());
                asistencia.setPresente(asistenciaActualizada.getPresente());
                return asistenciaRepository.save(asistencia);
            })
            .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con id: " + id));
    }
    
    // Eliminar asistencia
    public void eliminar(Long id) {
        asistenciaRepository.deleteById(id);
    }
    
    // Buscar por nombre
    public List<Asistencia> buscarPorNombre(String nombre) {
        return asistenciaRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Buscar por apellido
    public List<Asistencia> buscarPorApellido(String apellido) {
        return asistenciaRepository.findByApellidoContainingIgnoreCase(apellido);
    }
    
    // Buscar por estado de presencia
    public List<Asistencia> buscarPorPresente(Boolean presente) {
        return asistenciaRepository.findByPresente(presente);
    }
    
    // Buscar por rango de fechas
    public List<Asistencia> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return asistenciaRepository.findByFechaHoraBetween(inicio, fin);
    }
}
