package com.proyecto.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "universidades")
public class Universidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rankQS;
    private String nombre;
    private String ubicacion;

    private Double academicReputationScore;
    private Double employerReputationScore;
    private Double facultyStudentScore;
    private Double citationsPerFacultyScore;
    private Double internationalFacultyScore;
    private Double internationalStudentsScore;
    private Double internationalResearchNetworkScore;
    private Double employmentOutcomesScore;
    private Double sustainabilityScore;
    private Double overallScore;

    public Universidad() {}

    public Universidad(Long id, String rankQS, String nombre, String ubicacion,
                       Double academicReputationScore, Double employerReputationScore,
                       Double facultyStudentScore, Double citationsPerFacultyScore,
                       Double internationalFacultyScore, Double internationalStudentsScore,
                       Double internationalResearchNetworkScore, Double employmentOutcomesScore,
                       Double sustainabilityScore, Double overallScore) {
        this.id = id;
        this.rankQS = rankQS;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.academicReputationScore = academicReputationScore;
        this.employerReputationScore = employerReputationScore;
        this.facultyStudentScore = facultyStudentScore;
        this.citationsPerFacultyScore = citationsPerFacultyScore;
        this.internationalFacultyScore = internationalFacultyScore;
        this.internationalStudentsScore = internationalStudentsScore;
        this.internationalResearchNetworkScore = internationalResearchNetworkScore;
        this.employmentOutcomesScore = employmentOutcomesScore;
        this.sustainabilityScore = sustainabilityScore;
        this.overallScore = overallScore;
    }

    // Getters y setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRankQS() { return rankQS; }
    public void setRankQS(String rankQS) { this.rankQS = rankQS; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public Double getAcademicReputationScore() { return academicReputationScore; }
    public void setAcademicReputationScore(Double academicReputationScore) { this.academicReputationScore = academicReputationScore; }

    public Double getEmployerReputationScore() { return employerReputationScore; }
    public void setEmployerReputationScore(Double employerReputationScore) { this.employerReputationScore = employerReputationScore; }

    public Double getFacultyStudentScore() { return facultyStudentScore; }
    public void setFacultyStudentScore(Double facultyStudentScore) { this.facultyStudentScore = facultyStudentScore; }

    public Double getCitationsPerFacultyScore() { return citationsPerFacultyScore; }
    public void setCitationsPerFacultyScore(Double citationsPerFacultyScore) { this.citationsPerFacultyScore = citationsPerFacultyScore; }

    public Double getInternationalFacultyScore() { return internationalFacultyScore; }
    public void setInternationalFacultyScore(Double internationalFacultyScore) { this.internationalFacultyScore = internationalFacultyScore; }

    public Double getInternationalStudentsScore() { return internationalStudentsScore; }
    public void setInternationalStudentsScore(Double internationalStudentsScore) { this.internationalStudentsScore = internationalStudentsScore; }

    public Double getInternationalResearchNetworkScore() { return internationalResearchNetworkScore; }
    public void setInternationalResearchNetworkScore(Double internationalResearchNetworkScore) { this.internationalResearchNetworkScore = internationalResearchNetworkScore; }

    public Double getEmploymentOutcomesScore() { return employmentOutcomesScore; }
    public void setEmploymentOutcomesScore(Double employmentOutcomesScore) { this.employmentOutcomesScore = employmentOutcomesScore; }

    public Double getSustainabilityScore() { return sustainabilityScore; }
    public void setSustainabilityScore(Double sustainabilityScore) { this.sustainabilityScore = sustainabilityScore; }

    public Double getOverallScore() { return overallScore; }
    public void setOverallScore(Double overallScore) { this.overallScore = overallScore; }
}
