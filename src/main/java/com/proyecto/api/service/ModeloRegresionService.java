package com.proyecto.api.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModeloRegresionService {

    /**
     * Simula la predicción del Overall_Score usando un modelo de regresión.
     * Puedes ajustar la lógica según las columnas reales que tengas.
     *
     * @param features Mapa con las características de entrada.
     * @return Predicción del Overall_Score (valor entre 0 y 100).
     */
    public double predecirOverallScore(Map<String, Double> features) {
        double score = 0.0;

        // Lógica simple: suma ponderada de características
        for (Map.Entry<String, Double> entry : features.entrySet()) {
            score += entry.getValue() * 0.1;  // Coeficiente ficticio
        }

        // Normaliza el resultado al rango 0-100
        return Math.min(100, Math.max(0, score));
    }

    /**
     * Evalúa el modelo de regresión con métricas estándar: MAE, RMSE y R².
     *
     * @param valoresReales     Lista con los scores reales.
     * @param valoresPredichos  Lista con los scores predichos.
     * @return Mapa con las métricas calculadas.
     */
    public Map<String, Double> evaluarModelo(List<Double> valoresReales, List<Double> valoresPredichos) {
        if (valoresReales.size() != valoresPredichos.size() || valoresReales.isEmpty()) {
            throw new IllegalArgumentException("Listas de valores reales y predichos inválidas.");
        }

        int n = valoresReales.size();
        double sumaErrores = 0.0;
        double sumaErroresCuadrados = 0.0;
        double sumaReales = 0.0;
        double sumaTotales = 0.0;

        // Cálculo de errores
        for (int i = 0; i < n; i++) {
            double real = valoresReales.get(i);
            double predicho = valoresPredichos.get(i);
            double error = real - predicho;

            sumaErrores += Math.abs(error);              // MAE
            sumaErroresCuadrados += Math.pow(error, 2);  // RMSE
            sumaReales += real;
        }

        // Media de los valores reales
        double media = sumaReales / n;

        // Cálculo de la suma total de cuadrados (para R²)
        for (double real : valoresReales) {
            sumaTotales += Math.pow(real - media, 2);
        }

        // Métricas
        double mae = sumaErrores / n;
        double rmse = Math.sqrt(sumaErroresCuadrados / n);
        double r2 = 1 - (sumaErroresCuadrados / sumaTotales);

        // Retorna las métricas en un mapa
        Map<String, Double> resultados = new HashMap<>();
        resultados.put("MAE", mae);
        resultados.put("RMSE", rmse);
        resultados.put("R2", r2);

        return resultados;
    }
    
    
    
}
