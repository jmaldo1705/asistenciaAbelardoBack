package com.asistencia.service;

import com.asistencia.model.Coordinador;
import com.asistencia.repository.CoordinadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoordinadorService {
    
    @Autowired
    private CoordinadorRepository coordinadorRepository;
    
    public List<Coordinador> obtenerTodos() {
        List<Coordinador> coordinadores = coordinadorRepository.findAll();
        // Forzar la carga de las llamadas para evitar problemas de lazy loading
        coordinadores.forEach(c -> c.getLlamadas().size());
        return coordinadores;
    }
    
    public Optional<Coordinador> obtenerPorId(Long id) {
        Optional<Coordinador> coordinador = coordinadorRepository.findById(id);
        // Forzar la carga de las llamadas
        coordinador.ifPresent(c -> c.getLlamadas().size());
        return coordinador;
    }
    
    public List<Coordinador> buscarPorMunicipio(String municipio) {
        List<Coordinador> coordinadores = coordinadorRepository.findByMunicipioContainingIgnoreCase(municipio);
        coordinadores.forEach(c -> c.getLlamadas().size());
        return coordinadores;
    }
    
    public List<Coordinador> buscarPorEstadoConfirmacion(Boolean confirmado) {
        List<Coordinador> coordinadores = coordinadorRepository.findByConfirmado(confirmado);
        coordinadores.forEach(c -> c.getLlamadas().size());
        return coordinadores;
    }
    
    public Coordinador guardar(Coordinador coordinador) {
        return coordinadorRepository.save(coordinador);
    }
    
    public void eliminar(Long id) {
        coordinadorRepository.deleteById(id);
    }
    
    public long contarTotal() {
        return coordinadorRepository.count();
    }
}
