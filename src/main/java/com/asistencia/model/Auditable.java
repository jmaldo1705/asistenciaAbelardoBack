package com.asistencia.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @CreatedBy
    @Column(name = "creado_por_id", updatable = false)
    private Long creadoPorId;

    @CreatedDate
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @LastModifiedBy
    @Column(name = "modificado_por_id")
    private Long modificadoPorId;

    @LastModifiedDate
    @Column(name = "modificado_en")
    private LocalDateTime modificadoEn;

    // Getters y Setters
    public Long getCreadoPorId() {
        return creadoPorId;
    }

    public void setCreadoPorId(Long creadoPorId) {
        this.creadoPorId = creadoPorId;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public Long getModificadoPorId() {
        return modificadoPorId;
    }

    public void setModificadoPorId(Long modificadoPorId) {
        this.modificadoPorId = modificadoPorId;
    }

    public LocalDateTime getModificadoEn() {
        return modificadoEn;
    }

    public void setModificadoEn(LocalDateTime modificadoEn) {
        this.modificadoEn = modificadoEn;
    }
}
