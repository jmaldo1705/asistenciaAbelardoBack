package com.asistencia.dto;

import java.util.List;

public class LoginResponse {
    
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String nombreCompleto;
    private List<String> roles;
    private Boolean debeCambiarPassword;
    
    public LoginResponse(String token, Long id, String username, String email, 
                        String nombreCompleto, List<String> roles, Boolean debeCambiarPassword) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.roles = roles;
        this.debeCambiarPassword = debeCambiarPassword;
    }
    
    // Getters y Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
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
    
    public List<String> getRoles() {
        return roles;
    }
    
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    public Boolean getDebeCambiarPassword() {
        return debeCambiarPassword;
    }
    
    public void setDebeCambiarPassword(Boolean debeCambiarPassword) {
        this.debeCambiarPassword = debeCambiarPassword;
    }
}
