package com.proyecto.api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneradorHash {
    public static void main(String[] args) {
        String rawPassword = "admin123"; // Cambia la contraseña aquí si lo deseas
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(rawPassword);
        System.out.println("Hash generado para guardar en SQL:");
        System.out.println(hashedPassword);
    }
}
