package com.asistencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class AsistenciaApplication {

    @PostConstruct
    public void init() {
        // Configurar la zona horaria de Colombia para toda la aplicación
        TimeZone.setDefault(TimeZone.getTimeZone("America/Bogota"));
        System.out.println("🕐 Zona horaria configurada: " + TimeZone.getDefault().getID());
    }

    public static void main(String[] args) {
        SpringApplication.run(AsistenciaApplication.class, args);
    }

}
