package com.proyecto.api.config;

import com.proyecto.api.security.JwtAuthenticationFilter;
import com.proyecto.api.security.UserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // 🔐 Encriptador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Proveedor de autenticación con nuestro UserDetailsService
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // ✅ AuthenticationManager compatible con Spring Boot 3+
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 🔐 Configuración del filtro de seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/api/user/registrar").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/universidad/prediccion").permitAll()
               // .requestMatchers(HttpMethod.GET, "/api/universidad/csv").hasAnyRole("ADMIN") // o solo "ADMIN"
              //  .requestMatchers(HttpMethod.GET, "/api/user/listar").hasRole("ADMIN") // ✅ LÍNEA NUEVA
               // .requestMatchers("/api/user/listar").hasRole("ADMIN")  // ← SIN prefijo "ROLE_"
              //  .requestMatchers("/api/user/debug-rol").authenticated()
                
                
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/universidades/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/universidades/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/universidades/**").hasAnyRole("ADMIN", "USUARIO")
                .requestMatchers(HttpMethod.POST, "/api/universidad/preguntas").permitAll() // ← AÑADE ESTO
              //  .requestMatchers(HttpMethod.POST, "/api/universidad/preguntas").authenticated()
                .anyRequest().authenticated()
            )
            
            ///
            
            .exceptionHandling(ex -> ex
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\": \"Acceso denegado: no tienes permiso para acceder a este recurso\"}");
                    })
                )
            
            ////
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                corsConfig.setAllowedOriginPatterns(List.of("*")); // Permitir todas las fuentes
                corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                corsConfig.setAllowedHeaders(List.of("*"));
                corsConfig.setAllowCredentials(true);
                return corsConfig;
            }))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    
    
}
