package com.asistencia.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    
    @Value("${app.jwt.secret:asistenciaAbelardoSecretKeyMinimo32CaracteresParaHS256Seguridad2025}")
    private String jwtSecret;
    
    @Value("${app.jwt.expiration:86400000}") // 24 horas por defecto
    private int jwtExpiration;
    
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public String generateToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        
        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("id", userPrincipal.getId())
                .claim("email", userPrincipal.getEmail())
                .claim("nombreCompleto", userPrincipal.getNombreCompleto())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
    
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return claims.getSubject();
    }
    
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Firma JWT inválida");
        } catch (MalformedJwtException ex) {
            logger.error("Token JWT mal formado");
        } catch (ExpiredJwtException ex) {
            logger.error("Token JWT expirado");
        } catch (UnsupportedJwtException ex) {
            logger.error("Token JWT no soportado");
        } catch (IllegalArgumentException ex) {
            logger.error("Claims JWT vacío");
        }
        return false;
    }
}
