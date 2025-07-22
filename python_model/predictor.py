# -*- coding: utf-8 -*-
import pandas as pd
import numpy as np
import joblib
import os
import sys
import json
import warnings
from sklearn.metrics.pairwise import cosine_similarity

warnings.filterwarnings("ignore")

# === Paso 1: Obtener ruta robusta ===
try:
    ruta_actual = os.path.dirname(os.path.abspath(__file__))
except NameError:
    ruta_actual = os.getcwd()

# === Paso 2: Cargar modelo ===
try:
    ruta_modelo = os.path.join(ruta_actual, "modelo_regresion.pkl")
    modelo = joblib.load(ruta_modelo)
except Exception as e:
    print(json.dumps({"error": f"No se pudo cargar el modelo: {type(e).__name__}: {str(e)}"}))
    sys.exit(1)

# === Paso 3: Leer archivo JSON ===
try:
    json_path = sys.argv[1]
    with open(json_path, "r", encoding="utf-8") as f:
        entrada = json.load(f)
except Exception as e:
    print(json.dumps({"error": f"No se pudo leer el archivo JSON: {type(e).__name__}: {str(e)}"}))
    sys.exit(1)

# === Paso 4: Leer dataset CSV ===
try:
    ruta_dataset = os.path.join(ruta_actual, "QS World University Rankings 2025 (Top global universities).csv")
    df = pd.read_csv(ruta_dataset, encoding='latin1')
    df.columns = df.columns.str.strip()
except Exception as e:
    print(json.dumps({"error": f"No se pudo cargar el dataset CSV: {type(e).__name__}: {str(e)}"}))
    sys.exit(1)

# === Paso 5: Limpieza de columnas ===
def limpiar_rank(valor):
    try:
        if isinstance(valor, str):
            if '-' in valor:
                partes = valor.split('-')
                return (int(partes[0]) + int(partes[1])) / 2
            elif '+' in valor:
                return int(valor.replace('+', '')) + 5
        return float(valor)
    except:
        return np.nan

def limpiar_score(valor):
    try:
        return float(valor)
    except:
        return np.nan

df["RANK_2025"] = df["RANK_2025"].apply(limpiar_rank)
df["Overall_Score"] = df["Overall_Score"].apply(limpiar_score)

# === Paso 6: Validación de columnas ===
columnas_modelo = [
    "RANK_2025",
    "Academic_Reputation_Score",
    "Employer_Reputation_Score",
    "Sustainability_Score",
    "International_Research_Network_Score"
]

faltantes = [col for col in columnas_modelo if col not in df.columns]
if faltantes:
    print(json.dumps({"error": f"Faltan columnas requeridas en el dataset: {faltantes}"}))
    sys.exit(1)

df.dropna(subset=columnas_modelo + ["Overall_Score"], inplace=True)
df_features = df[columnas_modelo]
df_modelo = df.copy()

# === Paso 7: Convertir entrada en DataFrame ===
try:
    features = [
        float(entrada["RANK_2025"]),
        float(entrada["Academic_Reputation_Score"]),
        float(entrada["Employer_Reputation_Score"]),
        float(entrada["Sustainability_Score"]),
        float(entrada["International_Research_Network_Score"])
    ]
    df_entrada = pd.DataFrame([{
        "RANK_2025": features[0],
        "Academic_Reputation_Score": features[1],
        "Employer_Reputation_Score": features[2],
        "Sustainability_Score": features[3],
        "International_Research_Network_Score": features[4]
    }])
except Exception as e:
    print(json.dumps({"error": f"Entrada inválida: {type(e).__name__}: {str(e)}"}))
    sys.exit(1)

# === Paso 8: Hacer predicción ===
try:
    prediccion = modelo.predict(df_entrada)[0]
except Exception as e:
    print(json.dumps({"error": f"Error al predecir: {type(e).__name__}: {str(e)}"}))
    sys.exit(1)

# === Paso 9: Evaluar el resultado ===
def interpretar_prediccion(score):
    if score >= 90:
        return "Excelente"
    elif score >= 80:
        return "Rendimiento aceptable"
    elif score >= 70:
        return "Regular"
    else:
        return "Bajo rendimiento"

interpretacion = interpretar_prediccion(prediccion)

# === Paso 10: Universidad más similar ===
try:
    matriz = df_features.values
    entrada_array = df_entrada[columnas_modelo].values
    similitudes = cosine_similarity(entrada_array, matriz)
    idx = np.argmax(similitudes)
    uni_similar = df_modelo.iloc[idx]
except Exception as e:
    print(json.dumps({"error": f"Error al calcular similitud: {type(e).__name__}: {str(e)}"}))
    sys.exit(1)

# === Paso 11: Calcular percentil y universidades cercanas ===
all_scores = df_modelo["Overall_Score"].dropna().astype(float)
percentil = np.round((all_scores < prediccion).mean() * 100, 2)
umbral = 1.0
universidades_cercanas = df_modelo[
    df_modelo["Overall_Score"].between(prediccion - umbral, prediccion + umbral)
]["Institution_Name"].dropna().unique()

# === Paso 12: Generar salida ===
resultado = {
    "puntaje_estimado": round(prediccion, 2),
    "evaluacion": interpretacion,
    "percentil": percentil,
    "universidades_comparables": list(universidades_cercanas[:3]),
    "universidad_similar": {
        "nombre": uni_similar['Institution_Name'],
        "ranking_real": uni_similar['RANK_2025'],
        "puntaje_real": uni_similar['Overall_Score']
    }
}

print(json.dumps(resultado, ensure_ascii=False))