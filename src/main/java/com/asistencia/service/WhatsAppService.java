package com.asistencia.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class WhatsAppService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String whatsappFrom;

    @PostConstruct
    public void init() {
        if (accountSid != null && authToken != null && !accountSid.isEmpty() && !authToken.isEmpty()) {
            Twilio.init(accountSid, authToken);
        }
    }

    /**
     * Envía un mensaje de WhatsApp a un número específico
     * @param to Número de teléfono destino en formato E.164 (ej: +573001234567)
     * @param mensaje Contenido del mensaje
     * @return SID del mensaje enviado
     * @throws Exception Si hay un error al enviar el mensaje
     */
    public String enviarMensaje(String to, String mensaje) throws Exception {
        try {
            // Validar que las credenciales estén configuradas
            if (accountSid == null || authToken == null || accountSid.isEmpty() || authToken.isEmpty()) {
                throw new IllegalStateException("Las credenciales de Twilio no están configuradas. Por favor, configure TWILIO_ACCOUNT_SID y TWILIO_AUTH_TOKEN.");
            }

            if (whatsappFrom == null || whatsappFrom.isEmpty()) {
                throw new IllegalStateException("El número de WhatsApp de Twilio no está configurado. Por favor, configure TWILIO_WHATSAPP_FROM.");
            }

            // Asegurar que el número tenga el formato correcto
            String numeroFormateado = formatearNumero(to);

            // Enviar mensaje
            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + numeroFormateado),
                    new PhoneNumber("whatsapp:" + whatsappFrom),
                    mensaje
            ).create();

            return message.getSid();
        } catch (Exception e) {
            System.err.println("Error al enviar mensaje de WhatsApp: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Error al enviar mensaje de WhatsApp: " + e.getMessage(), e);
        }
    }

    /**
     * Formatea el número de teléfono al formato E.164
     * @param numero Número de teléfono (puede tener varios formatos)
     * @return Número formateado en E.164
     */
    private String formatearNumero(String numero) {
        // Remover espacios, guiones, paréntesis y otros caracteres
        String numeroLimpio = numero.replaceAll("[^0-9+]", "");

        // Si no empieza con +, asumir que es un número colombiano y agregar +57
        if (!numeroLimpio.startsWith("+")) {
            // Si empieza con 57, agregar +
            if (numeroLimpio.startsWith("57")) {
                numeroLimpio = "+" + numeroLimpio;
            } else {
                // Si empieza con 0, removerlo y agregar +57
                if (numeroLimpio.startsWith("0")) {
                    numeroLimpio = "+57" + numeroLimpio.substring(1);
                } else {
                    // Asumir que es un número colombiano sin código de país
                    numeroLimpio = "+57" + numeroLimpio;
                }
            }
        }

        return numeroLimpio;
    }
}

