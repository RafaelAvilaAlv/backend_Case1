package com.proyecto.api.repository;

import com.proyecto.api.model.Universidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversidadRepository extends JpaRepository<Universidad, Long> {
    // Puedes agregar métodos personalizados si necesitas más adelante
}
