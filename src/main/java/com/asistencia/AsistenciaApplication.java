package com.asistencia;

import com.asistencia.model.Rol;
import com.asistencia.model.Usuario;
import com.asistencia.repository.RolRepository;
import com.asistencia.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
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

    @Bean
    public CommandLineRunner initData(RolRepository rolRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Crear roles si no existen
            if (rolRepository.count() == 0) {
                System.out.println("📝 Creando roles iniciales...");
                Rol admin = new Rol();
                admin.setNombre("ADMINISTRADOR");
                admin.setDescripcion("Acceso completo al sistema incluyendo gestión de usuarios y exportación de datos");
                admin.setActivo(true);
                rolRepository.save(admin);

                Rol editor = new Rol();
                editor.setNombre("EDITOR");
                editor.setDescripcion("Acceso a operaciones CRUD sobre datos operativos, sin exportación de Excel");
                editor.setActivo(true);
                rolRepository.save(editor);

                Rol visor = new Rol();
                visor.setNombre("VISOR");
                visor.setDescripcion("Solo lectura de datos operativos, sin capacidad de crear, editar o eliminar");
                visor.setActivo(true);
                rolRepository.save(visor);
                
                System.out.println("✅ Roles creados exitosamente");
            }

            // Crear usuario admin si no existe
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                System.out.println("📝 Creando usuario administrador inicial...");
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("Admin2025$Temp"));
                admin.setEmail("admin@asistencia.com");
                admin.setNombreCompleto("Administrador del Sistema");
                admin.setActivo(true);
                admin.setDebeCambiarPassword(true);
                
                // Asignar rol ADMINISTRADOR
                Rol rolAdmin = rolRepository.findByNombre("ADMINISTRADOR")
                        .orElseThrow(() -> new RuntimeException("Rol ADMINISTRADOR no encontrado"));
                Set<Rol> roles = new HashSet<>();
                roles.add(rolAdmin);
                admin.setRoles(roles);
                
                usuarioRepository.save(admin);
                System.out.println("✅ Usuario admin creado exitosamente");
                System.out.println("   Username: admin");
                System.out.println("   Password: Admin2025$Temp");
                System.out.println("   ⚠️  Debe cambiar la contraseña en el primer login");
            }
        };
    }

}
