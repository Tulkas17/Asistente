// src/main/java/com/tuempresa/asistente_tareas/repository/TareaRepository.java
package com.asistente.asitentetareas.repository;

import com.asistente.asitentetareas.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
}
