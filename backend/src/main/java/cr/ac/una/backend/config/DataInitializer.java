package cr.ac.una.backend.config;

import cr.ac.una.backend.entity.Clima;
import cr.ac.una.backend.entity.Dependencia;
import cr.ac.una.backend.entity.Tarea;
import cr.ac.una.backend.enumeration.CondicionClimatica;
import cr.ac.una.backend.enumeration.Estado;
import cr.ac.una.backend.enumeration.Prioridad;
import cr.ac.una.backend.repository.ClimaRepository;
import cr.ac.una.backend.repository.DependenciaRepository;
import cr.ac.una.backend.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.List;

@Configuration
public class DataInitializer {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private ClimaRepository climaRepository;

    @Autowired
    private DependenciaRepository dependenciaRepository;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            // Verifica si ya hay datos en la base de datos para evitar duplicados
            if (tareaRepository.count() == 0) {
                // Insertar tareas de prueba
                Tarea tarea1 = new Tarea(null, "comprar_comida", Prioridad.ALTA, 60, null, null, Estado.PENDIENTE, null, null, null, List.of(CondicionClimatica.SOLEADO));
                Tarea tarea2 = new Tarea(null, "limpiar_casa", Prioridad.MEDIA, 120, null, null, Estado.PENDIENTE, null, null, null, List.of(CondicionClimatica.INDEPENDIENTE));
                Tarea tarea3 = new Tarea(null, "informe", Prioridad.ALTA, 180, null, null, Estado.PENDIENTE, null, null, null, List.of(CondicionClimatica.INDEPENDIENTE));
                tareaRepository.saveAll(List.of(tarea1, tarea2, tarea3));

                // Agregar dependencias entre tareas
                Dependencia dep1 = new Dependencia(null, tarea2, tarea1); // limpiar_casa depende de comprar_comida
                dependenciaRepository.save(dep1);

                // Insertar condiciones climáticas de prueba
                Clima climaSoleado = new Clima(null, "SOLEADO", new Date());
                Clima climaNublado = new Clima(null, "NUBLADO", new Date());
                climaRepository.saveAll(List.of(climaSoleado, climaNublado));
            }
        };
    }
}
