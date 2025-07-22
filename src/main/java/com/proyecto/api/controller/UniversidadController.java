package com.proyecto.api.controller;

import com.proyecto.api.dto.UniversidadDTO;
import com.proyecto.api.model.Universidad;
import com.proyecto.api.service.ServicioUniversidad;
import com.proyecto.api.service.ModeloRegresionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController 
@RequestMapping("/universidad")
public class UniversidadController {

    @Autowired
    private ServicioUniversidad servicioUniversidad;

    @Autowired
    private ModeloRegresionService modeloRegresionService;

    // ✅ NUEVO: Endpoint público para frontend que lista universidades
    @GetMapping("/universidades")
    public List<Universidad> listarUniversidadesPublico() {
        return servicioUniversidad.obtenerTodas();
    }

    // Endpoint para cargar datos desde un archivo CSV
    @PostMapping("/cargar")
    public ResponseEntity<String> cargarCSV(@RequestParam("archivo") MultipartFile archivo) {
        String resultado = servicioUniversidad.guardarDesdeCSV(archivo);
        return ResponseEntity.ok(resultado);
    }

    // Endpoint para listar todas las universidades
    @GetMapping("/listar")
    public ResponseEntity<List<Universidad>> listarUniversidades() {
        List<Universidad> universidades = servicioUniversidad.obtenerTodas();
        return ResponseEntity.ok(universidades);
    }

    // Endpoint para predecir el Overall_Score
    @PostMapping("/predecir")
    public ResponseEntity<Double> predecirOverallScore(@RequestBody Map<String, Double> caracteristicas) {
        double resultado = modeloRegresionService.predecirOverallScore(caracteristicas);
        return ResponseEntity.ok(resultado);
    }

    // Crear universidad desde DTO con validación
    @PostMapping("/crear")
    public ResponseEntity<String> crearUniversidad(@Valid @RequestBody UniversidadDTO dto) {
        servicioUniversidad.crearDesdeDTO(dto);
        return ResponseEntity.ok("Universidad creada exitosamente");
    }

    // Evaluar modelo con métricas MAE, RMSE y R2
    @PostMapping("/modelo/evaluar")
    public ResponseEntity<Map<String, Double>> evaluarModelo(@RequestBody List<UniversidadDTO> datos) {
        List<Double> reales = datos.stream()
                .map(UniversidadDTO::getOverallScore)
                .toList();

        List<Double> predichos = datos.stream()
                .map(dto -> {
                    Map<String, Double> features = new HashMap<>();
                    features.put("teaching", dto.getTeaching());
                    features.put("research", dto.getResearch());
                    features.put("citations", dto.getCitations());
                    features.put("international", dto.getInternational());
                    features.put("industryIncome", dto.getIndustryIncome());
                    return modeloRegresionService.predecirOverallScore(features);
                })
                .toList();

        Map<String, Double> resultado = modeloRegresionService.evaluarModelo(reales, predichos);
        return ResponseEntity.ok(resultado);
    }
    
    
    
    
    @GetMapping("/csv")
    public ResponseEntity<List<Map<String, String>>> obtenerDatosDesdeCSV() {
        List<Map<String, String>> datos = servicioUniversidad.leerDesdeArchivoCSV();
        return ResponseEntity.ok(datos);
    }


}
