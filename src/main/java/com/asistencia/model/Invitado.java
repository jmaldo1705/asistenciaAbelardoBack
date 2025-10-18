package com.asistencia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "invitados")
public class Invitado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del invitado es obligatorio")
    @Column(nullable = false)
    private String nombre;
    
    @NotBlank(message = "La cédula es obligatoria")
    @Column(nullable = false)
    private String cedula;
    
    @NotBlank(message = "El teléfono es obligatorio")
    @Column(nullable = false)
    private String telefono;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinador_id", nullable = false)
    @JsonIgnore
    private Coordinador coordinador;
    
    // Constructor vacío
    public Invitado() {
    }
    
    // Constructor con parámetros
    public Invitado(String nombre, String cedula, String telefono) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
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
    
    public String getCedula() {
        return cedula;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public Coordinador getCoordinador() {
        return coordinador;
    }
    
    public void setCoordinador(Coordinador coordinador) {
        this.coordinador = coordinador;
    }
}
