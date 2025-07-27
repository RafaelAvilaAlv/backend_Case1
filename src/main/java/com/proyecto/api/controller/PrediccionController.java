package com.proyecto.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/api/universidad")
@CrossOrigin(origins = "http://localhost:4200")
public class PrediccionController {

    @PostMapping("/prediccion")
    public ResponseEntity<?> predecir(@RequestBody Map<String, Object> input) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Guardar entrada en archivo temporal
            File tempInput = File.createTempFile("entrada", ".json");
            mapper.writeValue(tempInput, input);

            // Ejecutar script Python en carpeta python_model
            ProcessBuilder pb = new ProcessBuilder("python3", "predictor.py", tempInput.getAbsolutePath());
            pb.directory(new File("python_model"));  // Aquí la carpeta correcta
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println("[PYTHON] " + line);
                output.append(line.trim());
            }

            int exitCode = process.waitFor();
            System.out.println("Código de salida del script Python: " + exitCode);
            tempInput.delete();

            if (exitCode != 0 || output.toString().isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Fallo al ejecutar predicción"));
            }

            Map<String, Object> resultado = mapper.readValue(output.toString(), Map.class);
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno: " + e.getMessage()));
        }
    }

    @PostMapping("/preguntas")
    public ResponseEntity<?> responderPregunta(@RequestBody Map<String, String> request) {
        String pregunta = request.get("pregunta").trim().toLowerCase();

        try {
            ProcessBuilder pb = new ProcessBuilder("python3", "consultas_qs.py", pregunta);
            pb.directory(new File("python_model"));  // CORRECCIÓN aquí
            pb.redirectErrorStream(true);

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println("[PYTHON] " + line);
                output.append(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Código de salida del script Python: " + exitCode);
            if (exitCode != 0 || output.toString().isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Fallo al procesar la pregunta"));
            }

            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> universidades = mapper.readValue(output.toString(), List.class);

            return ResponseEntity.ok(universidades);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al ejecutar el script Python: " + e.getMessage()));
        }
    }

}
