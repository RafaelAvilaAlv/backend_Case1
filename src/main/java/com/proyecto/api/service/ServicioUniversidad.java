package com.proyecto.api.service;

import com.proyecto.api.dto.UniversidadDTO;
import com.proyecto.api.model.Universidad;
import com.proyecto.api.repository.UniversidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServicioUniversidad {

    @Autowired
    private UniversidadRepository universidadRepository;

    public String guardarDesdeCSV(MultipartFile archivo) {
        List<Universidad> universidades = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {

            String linea;
            boolean esCabecera = true;

            while ((linea = lector.readLine()) != null) {
                if (esCabecera) {
                    esCabecera = false; // saltar cabecera
                    continue;
                }

                String[] datos = linea.split(",");

                if (datos.length >= 9) {  // validar que haya suficientes columnas
                    Universidad uni = new Universidad();
                    uni.setAcademicReputationScore(parsearDouble(datos[0]));
                    uni.setEmployerReputationScore(parsearDouble(datos[1]));
                    uni.setFacultyStudentScore(parsearDouble(datos[2]));
                    uni.setCitationsPerFacultyScore(parsearDouble(datos[3]));
                    uni.setInternationalFacultyScore(parsearDouble(datos[4]));
                    uni.setInternationalStudentsScore(parsearDouble(datos[5]));
                    uni.setInternationalResearchNetworkScore(parsearDouble(datos[6]));
                    uni.setEmploymentOutcomesScore(parsearDouble(datos[7]));
                    uni.setSustainabilityScore(parsearDouble(datos[8]));

                    // Puedes agregar el overallScore si tu CSV lo tiene en la posición 9
                    if (datos.length > 9) {
                        uni.setOverallScore(parsearDouble(datos[9]));
                    }

                    universidades.add(uni);
                }
            }

            universidadRepository.saveAll(universidades);
            return "Datos guardados exitosamente";

        } catch (Exception e) {
            return "Error al procesar el archivo: " + e.getMessage();
        }
    }

    private Double parsearDouble(String texto) {
        try {
            return Double.parseDouble(texto.trim());
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Universidad> obtenerTodas() {
        return universidadRepository.findAll();
    }
    
     
    
    
    public void crearDesdeDTO(UniversidadDTO dto) {
        Universidad universidad = new Universidad();

        // Mapeo de campos del DTO a la entidad
        universidad.setNombre(dto.getNombre());
        universidad.setOverallScore(dto.getOverallScore());

        // Guardar en la base de datos
        universidadRepository.save(universidad);
    }
    
    
    //agregado nuevo 
    
    

    
    public List<Map<String, String>> leerDesdeArchivoCSV() {
        List<Map<String, String>> lista = new ArrayList<>();
        String rutaAbsoluta = "C:\\Users\\Usuario\\Desktop\\Spring5\\Workspace\\UniversityScoreAPI\\python_model\\QS World University Rankings 2025 (Top global universities).csv";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaAbsoluta), StandardCharsets.UTF_8))) {
            String[] headers = br.readLine().split(",");
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(",", -1);
                Map<String, String> fila = new LinkedHashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    fila.put(headers[i], i < valores.length ? valores[i] : "");
                }
                lista.add(fila);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }


}
