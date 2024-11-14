package cr.ac.una.backend.repository;

import cr.ac.una.backend.entity.Tarea;
import cr.ac.una.backend.enumeration.Estado;
import cr.ac.una.backend.enumeration.Prioridad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    // 1. Encontrar tareas por prioridad
    List<Tarea> findByPrioridad(Prioridad prioridad);

    // 2. Encontrar tareas por estado (ej., PENDIENTE, EN_PROGRESO, COMPLETADA)
    List<Tarea> findByEstado(Estado estado);

    // 3. Encontrar tareas con una fecha límite específica
    List<Tarea> findByFechaLimite(Date fechaLimite);

    // 4. Encontrar tareas con fecha límite antes de una fecha específica
    List<Tarea> findByFechaLimiteBefore(Date fechaLimite);

    // 5. Encontrar tareas con fecha límite después de una fecha específica
    List<Tarea> findByFechaLimiteAfter(Date fechaLimite);

    // 6. Encontrar tareas que dependan de otras tareas (que tengan dependencias)
    List<Tarea> findByDependenciasNotNull();

    // 7. Encontrar tareas por prioridad y estado específicos
    List<Tarea> findByPrioridadAndEstado(Prioridad prioridad, Estado estado);
}
