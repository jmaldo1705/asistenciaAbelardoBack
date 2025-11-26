package com.asistencia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "asistencias")
public class Asistencia extends Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false)
    private String apellido;
    
    @NotBlank(message = "El email es obligatorio")
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private LocalDateTime fechaHora;
    
    @Column(length = 500)
    private String observaciones;
    
    @Column(nullable = false)
    private Boolean presente;
    
    // Constructor vacío
    public Asistencia() {
        this.fechaHora = LocalDateTime.now();
        this.presente = true;
    }
    
    // Constructor con parámetros
    public Asistencia(String nombre, String apellido, String email, String observaciones, Boolean presente) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.observaciones = observaciones;
        this.presente = presente;
        this.fechaHora = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public Boolean getPresente() {
        return presente;
    }
    
    public void setPresente(Boolean presente) {
        this.presente = presente;
    }
}
