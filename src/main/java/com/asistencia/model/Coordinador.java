package com.asistencia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "coordinadores")
public class Coordinador extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String municipio;

    @Column
    private String sector;

    // Columna legacy - ignorada en inserts/updates pero necesaria para evitar errores de NOT NULL
    @Column(name = "ciudad", insertable = false, updatable = false)
    private String ciudad;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Column(nullable = false)
    private String nombreCompleto;

    @NotBlank(message = "El celular es obligatorio")
    @Column(nullable = false)
    private String celular;

    @Column
    private String email;

    @Column(unique = true)
    private String cedula;

    private LocalDateTime fechaLlamada;

    @Column(nullable = false)
    private Boolean confirmado;

    @NotNull(message = "El número de invitados es obligatorio")
    @Column(nullable = false)
    private Integer numeroInvitados;

    @Column(length = 500)
    private String observaciones;

    @Column(columnDefinition = "NUMERIC(10, 7)")
    private Double latitud;

    @Column(columnDefinition = "NUMERIC(10, 7)")
    private Double longitud;

    @OneToMany(mappedBy = "coordinador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Llamada> llamadas = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "coordinador_evento", joinColumns = @JoinColumn(name = "coordinador_id"), inverseJoinColumns = @JoinColumn(name = "evento_id"))
    @JsonIgnore
    private Set<Evento> eventos = new HashSet<>();

    // Constructor vacío
    public Coordinador() {
        this.confirmado = false;
        this.numeroInvitados = 0;
    }

    // Constructor con parámetros
    public Coordinador(String municipio, String sector, String nombreCompleto, String celular, String email) {
        this.municipio = municipio;
        this.sector = sector;
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

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
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

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
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

    @JsonIgnore
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
    @JsonIgnore
    public int getNumeroLlamadas() {
        return llamadas != null ? llamadas.size() : 0;
    }

    @JsonIgnore
    public Set<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(Set<Evento> eventos) {
        this.eventos = eventos;
    }
}
