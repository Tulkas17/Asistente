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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
public class DataInitializer {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private DependenciaRepository dependenciaRepository;

    @Autowired
    private ClimaRepository climaRepository;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            // Verifica si ya hay datos en la base de datos para evitar duplicados
            if (tareaRepository.count() == 0 && climaRepository.count() == 0) {
                // Insertar condiciones climáticas de prueba
                Clima soleado = new Clima(null, "soleado", new Date());
                Clima nublado = new Clima(null, "nublado", new Date());
                Clima lluvioso = new Clima(null, "lluvioso", new Date());
                Clima independiente = new Clima(null, "independiente", new Date());
                climaRepository.saveAll(List.of(soleado, nublado, lluvioso, independiente));

                // Crear instancias de fecha para prueba
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime futuro = now.plusDays(5);

                // Insertar tareas de prueba
                Tarea comprarComida = new Tarea(null, "comprar_comida", Prioridad.ALTA, 60, futuro, Estado.PENDIENTE, now, new ArrayList<>(), List.of(CondicionClimatica.SOLEADO));
                Tarea limpiarCasa = new Tarea(null, "limpiar_casa", Prioridad.MEDIA, 120, futuro, Estado.PENDIENTE, now, new ArrayList<>(), List.of(CondicionClimatica.INDEPENDIENTE));
                Tarea informe = new Tarea(null, "informe", Prioridad.ALTA, 180, futuro, Estado.PENDIENTE, now, new ArrayList<>(), List.of(CondicionClimatica.INDEPENDIENTE));
                Tarea repararAuto = new Tarea(null, "reparar_auto", Prioridad.ALTA, 240, futuro, Estado.PENDIENTE, now, new ArrayList<>(), List.of(CondicionClimatica.SOLEADO, CondicionClimatica.NUBLADO));
                Tarea plantarFlores = new Tarea(null, "plantar_flores", Prioridad.BAJA, 90, futuro, Estado.PENDIENTE, now, new ArrayList<>(), List.of(CondicionClimatica.SOLEADO));
                Tarea reunionEquipo = new Tarea(null, "reunion_equipo", Prioridad.ALTA, 60, futuro, Estado.PENDIENTE, now, new ArrayList<>(), List.of(CondicionClimatica.INDEPENDIENTE));
                Tarea entrenamiento = new Tarea(null, "entrenamiento", Prioridad.MEDIA, 90, futuro, Estado.PENDIENTE, now, new ArrayList<>(), List.of(CondicionClimatica.INDEPENDIENTE));
                Tarea lavadoDeRopa = new Tarea(null, "lavado_de_ropa", Prioridad.BAJA, 60, futuro, Estado.PENDIENTE, now, new ArrayList<>(), List.of(CondicionClimatica.NUBLADO, CondicionClimatica.LLUVIOSO));
                Tarea construirMueble = new Tarea(null, "construir_mueble", Prioridad.MEDIA, 150, futuro, Estado.PENDIENTE, now, new ArrayList<>(), List.of(CondicionClimatica.INDEPENDIENTE));
                Tarea jardineria = new Tarea(null, "jardineria", Prioridad.BAJA, 120, futuro, Estado.PENDIENTE, now, new ArrayList<>(), List.of(CondicionClimatica.SOLEADO));
                Tarea estudio = new Tarea(null, "estudio", Prioridad.ALTA, 180, futuro, Estado.PENDIENTE, now, new ArrayList<>(), List.of(CondicionClimatica.INDEPENDIENTE));
                Tarea citaMedica = new Tarea(null, "cita_medica", Prioridad.ALTA, 30, futuro, Estado.PENDIENTE, now, new ArrayList<>(), List.of(CondicionClimatica.INDEPENDIENTE));
                Tarea paseoPerro = new Tarea(null, "paseo_perro", Prioridad.MEDIA, 45, futuro, Estado.PENDIENTE, now, new ArrayList<>(), List.of(CondicionClimatica.SOLEADO, CondicionClimatica.NUBLADO));
                Tarea limpiarAuto = new Tarea(null, "limpiar_auto", Prioridad.BAJA, 40, futuro, Estado.PENDIENTE, now, new ArrayList<>(), List.of(CondicionClimatica.SOLEADO));
                Tarea mantenimientoJardin = new Tarea(null, "mantenimiento_jardin", Prioridad.MEDIA, 120, futuro, Estado.PENDIENTE, now, new ArrayList<>(), List.of(CondicionClimatica.SOLEADO, CondicionClimatica.NUBLADO));

                tareaRepository.saveAll(List.of(
                        comprarComida, limpiarCasa, informe, repararAuto, plantarFlores,
                        reunionEquipo, entrenamiento, lavadoDeRopa, construirMueble,
                        jardineria, estudio, citaMedica, paseoPerro, limpiarAuto, mantenimientoJardin
                ));

                // Agregar dependencias entre tareas
                Dependencia dependencia1 = new Dependencia(null, limpiarCasa, comprarComida);
                Dependencia dependencia2 = new Dependencia(null, plantarFlores, comprarComida);
                Dependencia dependencia3 = new Dependencia(null, entrenamiento, reunionEquipo);
                Dependencia dependencia4 = new Dependencia(null, construirMueble, limpiarCasa);
                Dependencia dependencia5 = new Dependencia(null, jardineria, plantarFlores);
                Dependencia dependencia6 = new Dependencia(null, limpiarAuto, lavadoDeRopa);
                Dependencia dependencia7 = new Dependencia(null, mantenimientoJardin, jardineria);

                dependenciaRepository.saveAll(List.of(dependencia1, dependencia2, dependencia3, dependencia4, dependencia5, dependencia6, dependencia7));
            }
        };
    }
}
