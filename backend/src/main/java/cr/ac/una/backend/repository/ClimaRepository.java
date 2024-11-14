package cr.ac.una.backend.repository;

import cr.ac.una.backend.entity.Clima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClimaRepository extends JpaRepository<Clima, Long> {

    // 1. Obtener el clima en una fecha y hora específica
    List<Clima> findByHora(Date hora);

    // 2. Obtener climas de un tipo específico (ej., soleado, lluvioso, etc.)
    List<Clima> findByTipo(String tipo);

    // 3. Obtener el historial de clima entre dos fechas específicas
    List<Clima> findByHoraBetween(Date start, Date end);

    // 4. Obtener el clima actual (último registro en la base de datos)
    Optional<Clima> findTopByOrderByHoraDesc();

    // 5. Guardar una nueva entrada de clima en el historial
    @Override
    Clima save(Clima clima);
}
