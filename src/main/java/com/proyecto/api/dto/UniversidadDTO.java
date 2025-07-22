package com.proyecto.api.dto;

import jakarta.validation.constraints.*;

public class UniversidadDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    private String nombre;

    @Min(value = 0, message = "El puntaje debe ser mayor o igual a 0")
    @Max(value = 100, message = "El puntaje no puede superar 100")
    private double overallScore;

    @Min(0) @Max(100)
    private double teaching;

    @Min(0) @Max(100)
    private double research;

    @Min(0) @Max(100)
    private double citations;

    @Min(0) @Max(100)
    private double international;

    @Min(0) @Max(100)
    private double industryIncome;

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(double overallScore) {
        this.overallScore = overallScore;
    }

    public double getTeaching() {
        return teaching;
    }

    public void setTeaching(double teaching) {
        this.teaching = teaching;
    }

    public double getResearch() {
        return research;
    }

    public void setResearch(double research) {
        this.research = research;
    }

    public double getCitations() {
        return citations;
    }

    public void setCitations(double citations) {
        this.citations = citations;
    }

    public double getInternational() {
        return international;
    }

    public void setInternational(double international) {
        this.international = international;
    }

    public double getIndustryIncome() {
        return industryIncome;
    }

    public void setIndustryIncome(double industryIncome) {
        this.industryIncome = industryIncome;
    }
}
