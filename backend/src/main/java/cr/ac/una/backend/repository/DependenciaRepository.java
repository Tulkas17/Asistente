package cr.ac.una.backend.repository;

import cr.ac.una.backend.entity.Dependencia;
import cr.ac.una.backend.entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DependenciaRepository extends JpaRepository<Dependencia, Long> {

    // 1. Obtener todas las dependencias de una tarea específica
    List<Dependencia> findByTarea(Tarea tarea);

    // 2. Obtener todas las tareas de las que depende una tarea específica
    List<Dependencia> findByTareaDependiente(Tarea tareaDependiente);

    // 3. Verificar si existe una dependencia específica entre dos tareas
    boolean existsByTareaAndTareaDependiente(Tarea tarea, Tarea tareaDependiente);

    // 4. Verificar si una tarea es dependiente de otra
    boolean existsByTareaDependiente(Tarea tareaDependiente);
}
