package cr.ac.una.springbootaopmaven.repository;

import cr.ac.una.springbootaopmaven.entity.Clima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ClimaRepository extends JpaRepository<Clima, Long> {

    // 1. Obtener el clima en una fecha y hora específicas
    List<Clima> findByHora(Date hora);

    // 2. Obtener climas de un tipo específico (ej., soleado, lluvioso, etc.)
    List<Clima> findByTipo(String tipo);

    // 3. Obtener el clima de una fecha específica
    List<Clima> findByHoraBetween(Date start, Date end);
}
