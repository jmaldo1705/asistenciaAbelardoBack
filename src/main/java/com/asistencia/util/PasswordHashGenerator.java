package com.asistencia.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String password = "Admin2025$Temp";
        String hash = encoder.encode(password);
        
        System.out.println("=================================");
        System.out.println("GENERADOR DE HASH BCRYPT");
        System.out.println("=================================");
        System.out.println("Password: " + password);
        System.out.println("Hash BCrypt: " + hash);
        System.out.println("=================================");
        System.out.println("\nPuedes usar este hash en la base de datos.");
    }
}
