package com.proyecto.api.controller;

import com.proyecto.api.model.Usuario;
import com.proyecto.api.service.ServicioAutenticacion;

import jakarta.validation.Valid;

import com.proyecto.api.dto.UsuarioRegistroDTO; // <-- IMPORT DEL DTO

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user") // <-- CAMBIADO para evitar conflicto con /api/auth
public class UsuarioController {

    @Autowired
    private ServicioAutenticacion servicioAutenticacion;

    // Registro de usuario
    @PostMapping("/registrar")
    public ResponseEntity<Map<String, String>> registrar(@Valid @RequestBody UsuarioRegistroDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setCorreo(dto.getCorreo());
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setContrasena(dto.getContrasena());

        usuario.setRol("USUARIO"); // 👈 FORZAR ROL SEGURO DESDE BACKEND

        boolean registrado = servicioAutenticacion.registrar(usuario);

        Map<String, String> respuesta = new HashMap<>();
        if (registrado) {
            respuesta.put("mensaje", "Usuario registrado exitosamente");
            return ResponseEntity.ok(respuesta);
        } else {
            respuesta.put("error", "El nombre de usuario ya existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }
    }
    
    
    // ✅ NUEVO: Endpoint para listar usuarios (solo ADMIN)
    @GetMapping("/listar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = servicioAutenticacion.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }
    

}
