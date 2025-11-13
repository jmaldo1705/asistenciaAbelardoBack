package com.asistencia.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    
    @Column
    private String ciudad;
    
    @Column
    private String municipio;
    
    @NotBlank(message = "El nombre completo es obligatorio")
    @Column(nullable = false)
    private String nombreCompleto;
    
    @NotBlank(message = "El celular es obligatorio")
    @Column(nullable = false)
    private String celular;
    
    @Column
    private String email;
    
    private LocalDateTime fechaLlamada;
    
    @Column(nullable = false)
    private Boolean confirmado;
    
    @NotNull(message = "El número de invitados es obligatorio")
    @Column(nullable = false)
    private Integer numeroInvitados;
    
    @Column(length = 500)
    private String observaciones;
    
    @Column(precision = 10, scale = 7)
    private Double latitud;
    
    @Column(precision = 10, scale = 7)
    private Double longitud;
    
    @OneToMany(mappedBy = "coordinador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Llamada> llamadas = new ArrayList<>();
    
    // Constructor vacío
    public Coordinador() {
        this.confirmado = false;
        this.numeroInvitados = 0;
    }
    
    // Constructor con parámetros
    public Coordinador(String ciudad, String municipio, String nombreCompleto, String celular, String email) {
        this.ciudad = ciudad;
        this.municipio = municipio;
        this.nombreCompleto = nombreCompleto;
        this.celular = celular;
        this.email = email;
        this.confirmado = false;
        this.numeroInvitados = 0;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCiudad() {
        return ciudad;
    }
    
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
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
    
    public Double getLatitud() {
        return latitud;
    }
    
    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }
    
    public Double getLongitud() {
        return longitud;
    }
    
    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
    
    public List<Llamada> getLlamadas() {
        return llamadas;
    }
    
    public void setLlamadas(List<Llamada> llamadas) {
        this.llamadas = llamadas;
    }
    
    // Método helper para agregar llamadas
    public void addLlamada(Llamada llamada) {
        llamadas.add(llamada);
        llamada.setCoordinador(this);
    }
    
    // Método helper para remover llamadas
    public void removeLlamada(Llamada llamada) {
        llamadas.remove(llamada);
        llamada.setCoordinador(null);
    }
    
    // Método para obtener el número de llamadas
    public int getNumeroLlamadas() {
        return llamadas != null ? llamadas.size() : 0;
    }
}
