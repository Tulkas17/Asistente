package cr.ac.una.backend.repository;

import cr.ac.una.backend.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {

    // Encontrar horarios en una fecha específica
    List<Horario> findByFecha(Date fecha);

    // Encontrar horarios que caen entre un rango de fechas
    List<Horario> findByFechaBetween(Date start, Date end);

    // Verificar si un horario específico ya existe en una fecha
    boolean existsByFecha(Date fecha);

    // Buscar horarios con tareas asignadas
    List<Horario> findByTareasNotNull();
}
