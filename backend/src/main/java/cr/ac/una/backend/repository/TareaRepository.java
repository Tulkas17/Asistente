package cr.ac.una.backend.repository;

import cr.ac.una.backend.entity.Tarea;
import cr.ac.una.backend.enumeration.Estado;
import cr.ac.una.backend.enumeration.Prioridad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    // Encontrar tareas por prioridad
    List<Tarea> findByPrioridad(Prioridad prioridad);

    // Encontrar tareas por estado (ej., PENDIENTE, EN_PROGRESO, COMPLETADA)
    List<Tarea> findByEstado(Estado estado);

    // Encontrar tareas con una fecha límite específica
    List<Tarea> findByFechaLimite(Date fechaLimite);

    // Encontrar tareas con fecha límite antes de una fecha específica
    List<Tarea> findByFechaLimiteBefore(Date fechaLimite);

    // Encontrar tareas con fecha límite después de una fecha específica
    List<Tarea> findByFechaLimiteAfter(Date fechaLimite);

    // Encontrar tareas que dependan de otras tareas (que tengan dependencias)
    List<Tarea> findByDependenciasNotNull();

    // Encontrar tareas por prioridad y estado específicos
    List<Tarea> findByPrioridadAndEstado(Prioridad prioridad, Estado estado);

    // Actualizar el estado de una tarea
    @Modifying
    @Query("UPDATE Tarea t SET t.estado = :estado WHERE t.id = :id")
    void actualizarEstadoTarea(@Param("id") Long id, @Param("estado") Estado estado);
}
