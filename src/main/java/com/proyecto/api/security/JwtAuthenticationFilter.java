package com.proyecto.api.security;

import com.proyecto.api.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 🔓 Rutas públicas que no requieren autenticación
        if (path.equals("/api/universidad/prediccion") ||
            path.startsWith("/api/auth") ||
            path.equals("/api/user/registrar")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. Obtener el header Authorization
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 2. Validar formato del token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer token y validarlo
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 4. Obtener usuario y rol del token
        String username = jwtUtil.getUsernameFromToken(token);
        String rol = jwtUtil.getRolFromToken(token);

        // 5. Crear autenticación con rol
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                username,
                null,
                List.of(new SimpleGrantedAuthority(rol.toUpperCase()))
            );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 6. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
