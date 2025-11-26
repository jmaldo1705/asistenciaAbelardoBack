package com.asistencia.controller;

import com.asistencia.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    @Autowired
    private WhatsAppService whatsAppService;

    /**
     * Endpoint para enviar un mensaje individual de WhatsApp
     */
    @PostMapping("/enviar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EDITOR')")
    public ResponseEntity<Map<String, Object>> enviarMensaje(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String to = request.get("to");
            String mensaje = request.get("mensaje");

            if (to == null || to.trim().isEmpty()) {
                response.put("success", false);
                response.put("error", "El número de teléfono es requerido");
                return ResponseEntity.badRequest().body(response);
            }

            if (mensaje == null || mensaje.trim().isEmpty()) {
                response.put("success", false);
                response.put("error", "El mensaje es requerido");
                return ResponseEntity.badRequest().body(response);
            }

            String messageSid = whatsAppService.enviarMensaje(to, mensaje);
            
            response.put("success", true);
            response.put("messageSid", messageSid);
            response.put("mensaje", "Mensaje enviado exitosamente");
            
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al enviar mensaje: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para enviar mensajes masivos de WhatsApp
     */
    @PostMapping("/enviar-masivo")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EDITOR')")
    public ResponseEntity<Map<String, Object>> enviarMensajesMasivos(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, String>> destinatarios = (List<Map<String, String>>) request.get("destinatarios");
            String mensaje = (String) request.get("mensaje");

            if (destinatarios == null || destinatarios.isEmpty()) {
                response.put("success", false);
                response.put("error", "Debe proporcionar al menos un destinatario");
                return ResponseEntity.badRequest().body(response);
            }

            if (mensaje == null || mensaje.trim().isEmpty()) {
                response.put("success", false);
                response.put("error", "El mensaje es requerido");
                return ResponseEntity.badRequest().body(response);
            }

            int exitosos = 0;
            int fallidos = 0;
            List<Map<String, Object>> resultados = new java.util.ArrayList<>();

            for (Map<String, String> destinatario : destinatarios) {
                String to = destinatario.get("celular");
                String nombre = destinatario.get("nombre");
                String mensajePersonalizado = personalizarMensaje(mensaje, destinatario);

                Map<String, Object> resultado = new HashMap<>();
                resultado.put("nombre", nombre);
                resultado.put("celular", to);

                try {
                    String messageSid = whatsAppService.enviarMensaje(to, mensajePersonalizado);
                    resultado.put("success", true);
                    resultado.put("messageSid", messageSid);
                    exitosos++;
                } catch (Exception e) {
                    resultado.put("success", false);
                    resultado.put("error", e.getMessage());
                    fallidos++;
                }

                resultados.add(resultado);
            }

            response.put("success", true);
            response.put("total", destinatarios.size());
            response.put("exitosos", exitosos);
            response.put("fallidos", fallidos);
            response.put("resultados", resultados);
            response.put("mensaje", String.format("Se enviaron %d mensajes exitosamente de %d totales", exitosos, destinatarios.size()));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al enviar mensajes masivos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Personaliza el mensaje reemplazando variables como {{nombre}}, {{municipio}}, {{sector}}
     */
    private String personalizarMensaje(String mensaje, Map<String, String> destinatario) {
        String mensajePersonalizado = mensaje;
        
        if (destinatario.containsKey("nombre")) {
            mensajePersonalizado = mensajePersonalizado.replace("{{nombre}}", destinatario.get("nombre"));
        }
        if (destinatario.containsKey("municipio")) {
            mensajePersonalizado = mensajePersonalizado.replace("{{municipio}}", destinatario.get("municipio") != null ? destinatario.get("municipio") : "");
        }
        if (destinatario.containsKey("sector")) {
            mensajePersonalizado = mensajePersonalizado.replace("{{sector}}", destinatario.get("sector") != null ? destinatario.get("sector") : "");
        }
        
        return mensajePersonalizado;
    }
}

