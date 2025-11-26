package com.asistencia.dto;

import java.time.LocalDateTime;

public class AuditoriaDTO {
    private Long id;
    private String entidad;
    private Long entidadId;
    private String accion;
    private String usuario;
    private LocalDateTime fecha;
    private String detalle;
    
    public AuditoriaDTO() {}
    
    public AuditoriaDTO(Long id, String entidad, Long entidadId, String accion, 
                       String usuario, LocalDateTime fecha, String detalle) {
        this.id = id;
        this.entidad = entidad;
        this.entidadId = entidadId;
        this.accion = accion;
        this.usuario = usuario;
        this.fecha = fecha;
        this.detalle = detalle;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEntidad() {
        return entidad;
    }
    
    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }
    
    public Long getEntidadId() {
        return entidadId;
    }
    
    public void setEntidadId(Long entidadId) {
        this.entidadId = entidadId;
    }
    
    public String getAccion() {
        return accion;
    }
    
    public void setAccion(String accion) {
        this.accion = accion;
    }
    
    public String getUsuario() {
        return usuario;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    public String getDetalle() {
        return detalle;
    }
    
    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
