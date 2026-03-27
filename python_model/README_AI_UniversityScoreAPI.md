
# UniversityScoreAPI - Módulo de Inteligencia Artificial

Este módulo contiene los scripts necesarios para entrenar, evaluar y utilizar un modelo de regresión para predecir el **puntaje global estimado (Overall Score)** de una universidad en base a sus características dentro del ranking QS World University Rankings 2025.

---

## 📁 Estructura de Archivos

```
python_model/
├── entrena_modelo.py     # Entrena el modelo con datos reales y guarda el .pkl
├── evalua_modelo.py      # Evalúa el modelo con métricas de regresión
├── predictor.py          # Predice el Overall Score a partir de entrada JSON
├── modelo_regresion.pkl  # Modelo ya entrenado en formato serializado
```

---

## 📌 Requisitos

- Python 3.8 o superior
- Bibliotecas:
  - `pandas`
  - `numpy`
  - `scikit-learn`
  - `matplotlib`
  - `joblib`

Instalación de dependencias:

```bash
pip install pandas numpy scikit-learn matplotlib joblib
```

---

## 🔁 Flujo de Trabajo

### 1. Entrenar el Modelo

```bash
python entrena_modelo.py
```

- Carga el dataset `QS World University Rankings 2025 (Top global universities).csv`.
- Limpia y transforma los datos (ej. convierte "12-18" en 15).
- Entrena un modelo `RandomForestRegressor`.
- Guarda el modelo como `modelo_regresion.pkl`.

---

### 2. Evaluar el Modelo

```bash
python evalua_modelo.py
```

- Muestra métricas como:
  - **MAE** (Error Absoluto Medio)
  - **MSE** (Error Cuadrático Medio)
  - **R²** (Coeficiente de Determinación)

---

### 3. Usar el Modelo para Predecir

```bash
python predictor.py entrada.json
```

- Recibe un archivo `entrada.json` con las siguientes claves:

```json
{
  "RANK_2025": 50,
  "Academic_Reputation_Score": 80.2,
  "Employer_Reputation_Score": 75.5,
  "Sustainability_Score": 60.0,
  "International_Research_Network_Score": 70.1
}
```

- Devuelve el puntaje global estimado (float redondeado).

---

## 🛡️ Validaciones y Manejo de Errores

- Todos los scripts usan `try-except` para manejo robusto de errores.
- Se emiten mensajes `[ERROR]` claros si:
  - Falla la lectura del archivo
  - El modelo no se encuentra
  - Los datos de entrada son inválidos
- Se incluyen mensajes `[INFO]` y `[DEBUG]` para seguimiento en consola.

---

## 🤖 Modelo Utilizado

- **Tipo:** `RandomForestRegressor` de `scikit-learn`
- **Entrenamiento supervisado** usando regresión multivariable
- **Target:** `Overall_Score`
- **Features:**
  - `RANK_2025`
  - `Academic_Reputation_Score`
  - `Employer_Reputation_Score`
  - `Sustainability_Score`
  - `International_Research_Network_Score`

---

## 📈 Resultado Esperado

El modelo predice con alta precisión el puntaje global de una universidad basándose en características clave de su desempeño académico y reputacional.

---

## 👨‍💻 Autor

Proyecto desarrollado como parte de un sistema integrado Spring Boot + Python para análisis educativo. Ideal para dashboards, rankings dinámicos o asesoría académica.

---


## En caso de tener problemas al correr usar primero estos comando en el forntend

pip install pandas
pip install joblib
pip install scikit-learn

---

