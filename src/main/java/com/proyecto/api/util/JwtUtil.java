package com.proyecto.api.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Clave secreta personalizada (debe tener al menos 32 caracteres para HS256)
    private final String SECRET_KEY = "clave_super_secreta_para_jwt_que_debe_ser_larga";
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora

    // Se construye la clave con el algoritmo HMAC
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // ✅ Generar token con usuario y rol
    public String generarToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .claim("rol", rol)
             // 🔧 CAMBIO: Agregamos el prefijo "ROLE_" para que Spring Security reconozca correctamente el rol
                //.claim("rol", "ROLE_" + rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Validar que el token no esté vencido o mal formado
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // ✅ Obtener nombre de usuario desde el token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // ✅ Obtener el rol desde el token
    public String getRolFromToken(String token) {
        return (String) Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("rol");
    }
}
