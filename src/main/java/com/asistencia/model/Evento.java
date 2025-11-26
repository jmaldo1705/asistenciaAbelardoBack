package com.asistencia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "eventos")
public class Evento extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del evento es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El lugar del evento es obligatorio")
    @Column(nullable = false)
    private String lugar;

    @NotNull(message = "La fecha del evento es obligatoria")
    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 500)
    private String observaciones;

    @ManyToMany(mappedBy = "eventos", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Coordinador> coordinadores = new HashSet<>();

    public Evento() {
    }

    public Evento(String nombre, String lugar, LocalDateTime fecha, String observaciones) {
        this.nombre = nombre;
        this.lugar = lugar;
        this.fecha = fecha;
        this.observaciones = observaciones;
    }

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

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Set<Coordinador> getCoordinadores() {
        return coordinadores;
    }

    public void setCoordinadores(Set<Coordinador> coordinadores) {
        this.coordinadores = coordinadores;
    }
}
