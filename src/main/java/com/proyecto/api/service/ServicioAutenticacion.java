package com.proyecto.api.service;

import com.proyecto.api.model.Usuario;
import com.proyecto.api.repository.UsuarioRepository;
import com.proyecto.api.security.UserDetailsServiceImpl;
import com.proyecto.api.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ServicioAutenticacion {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public String autenticar(String correo, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("❌ Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            throw new RuntimeException("❌ Contraseña incorrecta");
        }

        return jwtUtil.generarToken(usuario.getCorreo(), usuario.getRol());
    }

    public String obtenerRol(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .map(Usuario::getRol)
                .orElse("DESCONOCIDO");
    }

    public boolean registrar(Usuario usuario) {
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            return false;
        }

        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuarioRepository.save(usuario);
        return true;
    }

    public Map<String, String> login(String correo, String contrasena) {
        Map<String, String> response = new HashMap<>();
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(correo, contrasena)
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(correo);
            String token = jwtUtil.generarToken(
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority()
            );
            response.put("token", token);
        } catch (AuthenticationException e) {
            response.put("error", "Credenciales incorrectas");
        }
        return response;
    }
    
    
    
    // ✅ NUEVO método para listar usuarios
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }
}