package com.asistencia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CambiarPasswordRequest {
    
    @NotBlank(message = "La contraseña actual es obligatoria")
    private String passwordActual;
    
    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
    private String passwordNueva;
    
    @NotBlank(message = "La confirmación de contraseña es obligatoria")
    private String passwordConfirmacion;
    
    public CambiarPasswordRequest() {
    }
    
    public CambiarPasswordRequest(String passwordActual, String passwordNueva, String passwordConfirmacion) {
        this.passwordActual = passwordActual;
        this.passwordNueva = passwordNueva;
        this.passwordConfirmacion = passwordConfirmacion;
    }
    
    public String getPasswordActual() {
        return passwordActual;
    }
    
    public void setPasswordActual(String passwordActual) {
        this.passwordActual = passwordActual;
    }
    
    public String getPasswordNueva() {
        return passwordNueva;
    }
    
    public void setPasswordNueva(String passwordNueva) {
        this.passwordNueva = passwordNueva;
    }
    
    public String getPasswordConfirmacion() {
        return passwordConfirmacion;
    }
    
    public void setPasswordConfirmacion(String passwordConfirmacion) {
        this.passwordConfirmacion = passwordConfirmacion;
    }
}
