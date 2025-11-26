package com.asistencia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Column(nullable = false)
    private String nombreCompleto;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(nullable = false)
    private Boolean debeCambiarPassword = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column
    private LocalDateTime fechaUltimaModificacion;

    @Column
    private LocalDateTime ultimoLogin;

    @Column(nullable = false)
    private Integer intentosFallidos = 0;

    @Column(nullable = false)
    private Boolean cuentaBloqueada = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();

    // Constructor vacío
    public Usuario() {
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
        this.debeCambiarPassword = false;
        this.intentosFallidos = 0;
        this.cuentaBloqueada = false;
    }

    // Constructor con parámetros básicos
    public Usuario(String username, String password, String email, String nombreCompleto) {
        this();
        this.username = username;
        this.password = password;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaUltimaModificacion = LocalDateTime.now();
    }

    // Métodos helper para roles
    public void addRol(Rol rol) {
        this.roles.add(rol);
    }

    public void removeRol(Rol rol) {
        this.roles.remove(rol);
    }

    public boolean hasRol(String nombreRol) {
        return this.roles.stream()
                .anyMatch(rol -> rol.getNombre().equals(nombreRol));
    }

    // Métodos para gestión de intentos de login
    public void incrementarIntentosFallidos() {
        this.intentosFallidos++;
        if (this.intentosFallidos >= 5) {
            this.cuentaBloqueada = true;
        }
    }

    public void resetearIntentosFallidos() {
        this.intentosFallidos = 0;
        this.cuentaBloqueada = false;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getDebeCambiarPassword() {
        return debeCambiarPassword;
    }

    public void setDebeCambiarPassword(Boolean debeCambiarPassword) {
        this.debeCambiarPassword = debeCambiarPassword;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }

    public void setFechaUltimaModificacion(LocalDateTime fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }

    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(LocalDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    public Integer getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(Integer intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public Boolean getCuentaBloqueada() {
        return cuentaBloqueada;
    }

    public void setCuentaBloqueada(Boolean cuentaBloqueada) {
        this.cuentaBloqueada = cuentaBloqueada;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
}
