package com.asistencia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coordinadores")
public class Coordinador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El municipio es obligatorio")
    @Column(nullable = false)
    private String municipio;
    
    @NotBlank(message = "El nombre completo es obligatorio")
    @Column(nullable = false)
    private String nombreCompleto;
    
    @NotBlank(message = "El celular es obligatorio")
    @Column(nullable = false)
    private String celular;
    
    private LocalDateTime fechaLlamada;
    
    @Column(nullable = false)
    private Boolean confirmado;
    
    @NotNull(message = "El número de invitados es obligatorio")
    @Column(nullable = false)
    private Integer numeroInvitados;
    
    @Column(length = 500)
    private String observaciones;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstadoCoordinador estado;
    
    @OneToMany(mappedBy = "coordinador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Invitado> invitados = new ArrayList<>();
    
    // Constructor vacío
    public Coordinador() {
        this.confirmado = false;
        this.numeroInvitados = 0;
        this.estado = EstadoCoordinador.PENDIENTE;
    }
    
    // Constructor con parámetros
    public Coordinador(String municipio, String nombreCompleto, String celular) {
        this.municipio = municipio;
        this.nombreCompleto = nombreCompleto;
        this.celular = celular;
        this.confirmado = false;
        this.numeroInvitados = 0;
        this.estado = EstadoCoordinador.PENDIENTE;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMunicipio() {
        return municipio;
    }
    
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    public String getCelular() {
        return celular;
    }
    
    public void setCelular(String celular) {
        this.celular = celular;
    }
    
    public LocalDateTime getFechaLlamada() {
        return fechaLlamada;
    }
    
    public void setFechaLlamada(LocalDateTime fechaLlamada) {
        this.fechaLlamada = fechaLlamada;
    }
    
    public Boolean getConfirmado() {
        return confirmado;
    }
    
    public void setConfirmado(Boolean confirmado) {
        this.confirmado = confirmado;
    }
    
    public Integer getNumeroInvitados() {
        return numeroInvitados;
    }
    
    public void setNumeroInvitados(Integer numeroInvitados) {
        this.numeroInvitados = numeroInvitados;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public List<Invitado> getInvitados() {
        return invitados;
    }
    
    public void setInvitados(List<Invitado> invitados) {
        this.invitados = invitados;
    }
    
    public EstadoCoordinador getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoCoordinador estado) {
        this.estado = estado;
    }
    
    // Método helper para agregar invitados
    public void addInvitado(Invitado invitado) {
        invitados.add(invitado);
        invitado.setCoordinador(this);
    }
    
    // Método helper para remover invitados
    public void removeInvitado(Invitado invitado) {
        invitados.remove(invitado);
        invitado.setCoordinador(null);
    }
}
