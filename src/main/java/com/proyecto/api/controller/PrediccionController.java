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
            
            // Ruta del script Python
            String scriptPath = new File("python_model/predictor.py").getAbsolutePath();

            ProcessBuilder pb = new ProcessBuilder("python", scriptPath, tempInput.getAbsolutePath());
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
        //String pregunta = request.get("pregunta");
    	
    	String pregunta = request.get("pregunta").trim().toLowerCase();


        try {
            ProcessBuilder pb = new ProcessBuilder("python", "python_model/consultas_qs.py", pregunta);
            pb.directory(new File(System.getProperty("user.dir"))); // raíz del proyecto
            pb.redirectErrorStream(true);

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0 || output.toString().isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Fallo al procesar la pregunta"));
            }

            // Transformar la salida JSON del script a un objeto Java
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> universidades = mapper.readValue(output.toString(), List.class);

            return ResponseEntity.ok(universidades);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al ejecutar el script Python: " + e.getMessage()));
        }
    }


    
}
