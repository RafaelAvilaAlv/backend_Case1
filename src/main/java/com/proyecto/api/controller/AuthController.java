package com.proyecto.api.controller;

import com.proyecto.api.model.Usuario;
import com.proyecto.api.service.ServicioAutenticacion;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private ServicioAutenticacion servicioAutenticacion;

   

    @PostMapping("/registrar")
    public ResponseEntity<?> register(@Valid @RequestBody Usuario usuario) {
        boolean creado = servicioAutenticacion.registrar(usuario);
        if (creado) {
            return ResponseEntity.ok(Map.of("mensaje", "Usuario registrado"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "El correo ya está en uso"));
        }
    }

    
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        Map<String, String> loginResponse = servicioAutenticacion.login(usuario.getCorreo(), usuario.getContrasena());
        if (loginResponse.containsKey("token")) {
            return ResponseEntity.ok(loginResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

}
