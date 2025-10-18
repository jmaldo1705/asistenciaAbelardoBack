package com.asistencia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Bean
    @Primary
    public DataSource dataSource() {
        // Si DATABASE_URL existe (entorno Railway)
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            try {
                // Railway proporciona: postgresql://user:password@host:port/database
                // Necesitamos convertirlo a: jdbc:postgresql://host:port/database
                
                URI dbUri = new URI(databaseUrl);
                
                String username = "";
                String password = "";
                
                // Extraer usuario y contraseña
                if (dbUri.getUserInfo() != null) {
                    String[] userInfo = dbUri.getUserInfo().split(":");
                    username = userInfo[0];
                    password = userInfo.length > 1 ? userInfo[1] : "";
                }
                
                // Construir la URL JDBC
                String jdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort() + dbUri.getPath();
                
                System.out.println("🔗 Conectando a Railway PostgreSQL:");
                System.out.println("   Host: " + dbUri.getHost());
                System.out.println("   Puerto: " + dbUri.getPort());
                System.out.println("   Base de datos: " + dbUri.getPath());
                System.out.println("   Usuario: " + username);

                return DataSourceBuilder
                        .create()
                        .url(jdbcUrl)
                        .username(username)
                        .password(password)
                        .driverClassName("org.postgresql.Driver")
                        .build();
                        
            } catch (URISyntaxException e) {
                System.err.println("❌ Error al parsear DATABASE_URL: " + e.getMessage());
                throw new RuntimeException("Error al parsear DATABASE_URL", e);
            }
        }
        
        // Configuración por defecto para desarrollo local
        System.out.println("🔗 Conectando a PostgreSQL local:");
        System.out.println("   URL: jdbc:postgresql://localhost:5432/asistenciadb");
        System.out.println("   Usuario: admin");
        
        return DataSourceBuilder
                .create()
                .url("jdbc:postgresql://localhost:5432/asistenciadb")
                .username("admin")
                .password("admin.123")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
