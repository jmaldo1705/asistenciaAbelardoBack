package com.asistencia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "registro_auditoria")
public class RegistroAuditoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String entidad;
    
    @Column(nullable = false)
    private Long entidadId;
    
    @Column(nullable = false)
    private String accion;
    
    @Column(nullable = false)
    private String usuario;
    
    @Column(nullable = false)
    private LocalDateTime fecha;
    
    @Column(length = 1000)
    private String detalle;
    
    // Constructor vacío
    public RegistroAuditoria() {
        this.fecha = ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime();
    }
    
    // Constructor con parámetros
    public RegistroAuditoria(String entidad, Long entidadId, String accion, String usuario, String detalle) {
        this.entidad = entidad;
        this.entidadId = entidadId;
        this.accion = accion;
        this.usuario = usuario;
        this.detalle = detalle;
        this.fecha = ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime();
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
