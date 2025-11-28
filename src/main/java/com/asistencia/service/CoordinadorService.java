package com.asistencia.service;

import com.asistencia.model.Coordinador;
import com.asistencia.model.RegistroAuditoria;
import com.asistencia.repository.CoordinadorRepository;
import com.asistencia.repository.RegistroAuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoordinadorService {

    @Autowired
    private CoordinadorRepository coordinadorRepository;
    
    @Autowired
    private RegistroAuditoriaRepository registroAuditoriaRepository;

    /**
     * Compara dos coordinadores y genera un detalle de los cambios realizados
     */
    private String generarDetalleCambios(Coordinador anterior, Coordinador nuevo) {
        StringBuilder cambios = new StringBuilder("Coordinador: " + anterior.getNombreCompleto() + " - Cambios: ");
        boolean hayCambios = false;

        if (!esIgual(anterior.getNombreCompleto(), nuevo.getNombreCompleto())) {
            cambios.append("Nombre: '").append(anterior.getNombreCompleto())
                   .append("' → '").append(nuevo.getNombreCompleto()).append("'; ");
            hayCambios = true;
        }

        if (!esIgual(anterior.getMunicipio(), nuevo.getMunicipio())) {
            cambios.append("Municipio: '").append(anterior.getMunicipio())
                   .append("' → '").append(nuevo.getMunicipio()).append("'; ");
            hayCambios = true;
        }

        if (!esIgual(anterior.getSector(), nuevo.getSector())) {
            cambios.append("Sector: '").append(anterior.getSector())
                   .append("' → '").append(nuevo.getSector()).append("'; ");
            hayCambios = true;
        }

        if (!esIgual(anterior.getCelular(), nuevo.getCelular())) {
            cambios.append("Celular: '").append(anterior.getCelular())
                   .append("' → '").append(nuevo.getCelular()).append("'; ");
            hayCambios = true;
        }

        if (!esIgual(anterior.getEmail(), nuevo.getEmail())) {
            cambios.append("Email: '").append(anterior.getEmail())
                   .append("' → '").append(nuevo.getEmail()).append("'; ");
            hayCambios = true;
        }

        if (!esIgual(anterior.getCedula(), nuevo.getCedula())) {
            cambios.append("Cédula: '").append(anterior.getCedula())
                   .append("' → '").append(nuevo.getCedula()).append("'; ");
            hayCambios = true;
        }

        if (!esIgualDouble(anterior.getLatitud(), nuevo.getLatitud())) {
            cambios.append("Latitud: ").append(anterior.getLatitud())
                   .append(" → ").append(nuevo.getLatitud()).append("; ");
            hayCambios = true;
        }

        if (!esIgualDouble(anterior.getLongitud(), nuevo.getLongitud())) {
            cambios.append("Longitud: ").append(anterior.getLongitud())
                   .append(" → ").append(nuevo.getLongitud()).append("; ");
            hayCambios = true;
        }

        if (!hayCambios) {
            return "Coordinador: " + anterior.getNombreCompleto() + " - Sin cambios registrados";
        }

        // Eliminar el último "; "
        String resultado = cambios.toString();
        if (resultado.endsWith("; ")) {
            resultado = resultado.substring(0, resultado.length() - 2);
        }

        return resultado;
    }

    private boolean esIgual(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    private boolean esIgualDouble(Double a, Double b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return Math.abs(a - b) < 0.000001;
    }

    public Coordinador actualizarConAuditoria(Long id, Coordinador coordinadorNuevo) {
        Coordinador anterior = coordinadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coordinador no encontrado"));

        // Generar detalle de cambios
        String detalleCambios = generarDetalleCambios(anterior, coordinadorNuevo);

        // Actualizar los campos
        anterior.setMunicipio(coordinadorNuevo.getMunicipio());
        anterior.setSector(coordinadorNuevo.getSector());
        anterior.setNombreCompleto(coordinadorNuevo.getNombreCompleto());
        anterior.setCelular(coordinadorNuevo.getCelular());
        anterior.setEmail(coordinadorNuevo.getEmail());
        anterior.setCedula(coordinadorNuevo.getCedula());
        anterior.setLatitud(coordinadorNuevo.getLatitud());
        anterior.setLongitud(coordinadorNuevo.getLongitud());

        Coordinador actualizado = coordinadorRepository.save(anterior);

        // Registrar en auditoría solo si hubo cambios reales
        if (!detalleCambios.contains("Sin cambios registrados")) {
            String usuario = "Sistema";
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                usuario = auth.getName();
            }

            RegistroAuditoria registro = new RegistroAuditoria(
                "Coordinador",
                id,
                "MODIFICACIÓN",
                usuario,
                detalleCambios
            );

            registroAuditoriaRepository.save(registro);
        }

        return actualizado;
    }

    public List<Coordinador> obtenerTodos() {
        try {
            List<Coordinador> coordinadores = coordinadorRepository.findAll();
            return coordinadores;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener coordinadores: " + e.getMessage(), e);
        }
    }

    public Optional<Coordinador> obtenerPorId(Long id) {
        try {
            Optional<Coordinador> coordinador = coordinadorRepository.findById(id);
            // Inicializar colecciones lazy si es necesario
            coordinador.ifPresent(c -> {
                try {
                    if (c.getLlamadas() != null) {
                        c.getLlamadas().size();
                    }
                    if (c.getEventos() != null) {
                        c.getEventos().size();
                    }
                } catch (Exception e) {
                    System.err.println("⚠️ Error al cargar relaciones para coordinador " + id + ": " + e.getMessage());
                }
            });
            return coordinador;
        } catch (Exception e) {
            System.err.println("❌ Error en obtenerPorId: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Coordinador> buscarPorMunicipio(String municipio) {
        try {
            List<Coordinador> coordinadores = coordinadorRepository.findByMunicipioContainingIgnoreCase(municipio);
            return coordinadores;
        } catch (Exception e) {
            System.err.println("❌ Error en buscarPorMunicipio: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al buscar coordinadores por municipio", e);
        }
    }

    public List<Coordinador> buscarPorEstadoConfirmacion(Boolean confirmado) {
        try {
            List<Coordinador> coordinadores = coordinadorRepository.findByConfirmado(confirmado);
            return coordinadores;
        } catch (Exception e) {
            System.err.println("❌ Error en buscarPorEstadoConfirmacion: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al buscar coordinadores por estado", e);
        }
    }

    public Coordinador guardar(Coordinador coordinador) {
        boolean esNuevo = coordinador.getId() == null;
        Coordinador guardado = coordinadorRepository.save(coordinador);
        
        // Registrar creación en auditoría solo si es nuevo
        if (esNuevo) {
            String usuario = "Sistema";
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                usuario = auth.getName();
            }
            
            String detalle = "Coordinador: " + guardado.getNombreCompleto() + " - " + 
                           guardado.getMunicipio() + 
                           (guardado.getCedula() != null ? " (Cédula: " + guardado.getCedula() + ")" : "");
            
            RegistroAuditoria registro = new RegistroAuditoria(
                "Coordinador",
                guardado.getId(),
                "CREACIÓN",
                usuario,
                detalle
            );
            
            registroAuditoriaRepository.save(registro);
        }
        
        return guardado;
    }

    public void eliminar(Long id) {
        // Obtener información del coordinador antes de eliminarlo
        Optional<Coordinador> coordinadorOpt = coordinadorRepository.findById(id);
        
        if (coordinadorOpt.isPresent()) {
            Coordinador coordinador = coordinadorOpt.get();
            
            // Obtener usuario actual
            String usuario = "Sistema";
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                usuario = auth.getName();
            }
            
            // Registrar la eliminación en auditoría
            String detalle = "Coordinador eliminado: " + coordinador.getNombreCompleto() + 
                           " - " + coordinador.getMunicipio() + 
                           " (Cédula: " + coordinador.getCedula() + ")";
            
            RegistroAuditoria registro = new RegistroAuditoria(
                "Coordinador",
                id,
                "ELIMINACIÓN",
                usuario,
                detalle
            );
            
            registroAuditoriaRepository.save(registro);
        }
        
        // Eliminar el coordinador
        coordinadorRepository.deleteById(id);
    }

    public long contarTotal() {
        return coordinadorRepository.count();
    }

    @Autowired
    private com.asistencia.repository.EventoRepository eventoRepository;

    public void asignarEvento(Long coordinadorId, Long eventoId) {
        Coordinador coordinador = coordinadorRepository.findById(coordinadorId)
                .orElseThrow(() -> new RuntimeException("Coordinador no encontrado"));

        com.asistencia.model.Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        coordinador.getEventos().add(evento);
        coordinadorRepository.save(coordinador);
    }

    public void desasignarEvento(Long coordinadorId, Long eventoId) {
        Coordinador coordinador = coordinadorRepository.findById(coordinadorId)
                .orElseThrow(() -> new RuntimeException("Coordinador no encontrado"));

        com.asistencia.model.Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        coordinador.getEventos().remove(evento);
        coordinadorRepository.save(coordinador);
    }

    public List<Coordinador> obtenerPorEventoEnLlamadas(Long eventoId) {
        try {
            List<Coordinador> coordinadores = coordinadorRepository.findByLlamadasEventoId(eventoId);
            return coordinadores;
        } catch (Exception e) {
            System.err.println("❌ Error en obtenerPorEventoEnLlamadas: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al obtener coordinadores por evento", e);
        }
    }

    public boolean existePorCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return false;
        }
        return coordinadorRepository.existsByCedula(cedula);
    }

    public List<Coordinador> buscarPorCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return List.of();
        }
        return coordinadorRepository.findByCedula(cedula)
                .map(List::of)
                .orElse(List.of());
    }
}
