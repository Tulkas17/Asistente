package cr.ac.una.backend.service;

import cr.ac.una.backend.entity.Dependencia;
import cr.ac.una.backend.entity.Tarea;
import cr.ac.una.backend.enumeration.Estado;
import cr.ac.una.backend.prolog.PrologEngine;
import cr.ac.una.backend.prolog.PrologExecutionException;
import cr.ac.una.backend.repository.DependenciaRepository;
import cr.ac.una.backend.repository.TareaRepository;
import jakarta.transaction.Transactional;
import org.jpl7.Query;
import org.jpl7.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TareaService {

    private final PrologEngine prologEngine = new PrologEngine();
    @Autowired
    private TareaRepository tareaRepository;
    @Autowired
    private DependenciaRepository dependenciaRepository;
    @Autowired
    private ClimaService climaService;

    // Obtener todas las tareas
    public List<Tarea> obtenerTareas() {
        return tareaRepository.findAll();
    }

    // Obtener una tarea por ID
    public Tarea obtenerTareaPorId(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
    }

    // Obtener tareas por estado (pendientes, en progreso, completadas)
    public List<Tarea> obtenerTareasPorEstado(Estado estado) {
        return tareaRepository.findByEstado(estado);
    }

    // Crear una nueva tarea
    public Tarea crearTarea(Tarea tarea) {
        Tarea tareaGuardada = tareaRepository.save(tarea);

        if (tarea.getDependencias() != null && !tarea.getDependencias().isEmpty()) {
            List<Dependencia> nuevasDependencias = tarea.getDependencias().stream()
                    .filter(dep -> !existeDependencia(tareaGuardada.getId(), dep.getTareaDependiente().getId()))
                    .map(dep -> new Dependencia(null, tareaGuardada, obtenerTareaPorId(dep.getTareaDependiente().getId())))
                    .collect(Collectors.toList());

            dependenciaRepository.saveAll(nuevasDependencias);
            tareaGuardada.setDependencias(nuevasDependencias);
        }

        return tareaGuardada;
    }

    // Actualizar el estado de una tarea
    public void actualizarEstadoTarea(Long id, Estado nuevoEstado) {
        Tarea tarea = obtenerTareaPorId(id);
        tarea.setEstado(nuevoEstado);
        tareaRepository.save(tarea);
    }

    // Actualizar una tarea existente
    @Transactional
    public ResponseEntity<?> actualizarTarea(@PathVariable Long id, @RequestBody Tarea tareaActualizada) {
        Optional<Tarea> tareaOptional = tareaRepository.findById(id);
        if (tareaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Tarea tarea = tareaOptional.get();

        try {
            // Actualizar atributos básicos
            tarea.setNombre(tareaActualizada.getNombre());
            tarea.setPrioridad(tareaActualizada.getPrioridad());
            tarea.setTiempoEstimado(tareaActualizada.getTiempoEstimado());
            tarea.setFechaLimite(tareaActualizada.getFechaLimite());
            tarea.setFechaInicio(tareaActualizada.getFechaInicio());
            tarea.setEstado(tareaActualizada.getEstado());
            tarea.setCondicionesClimaticas(tareaActualizada.getCondicionesClimaticas());

            // Limpiar y reasignar dependencias
            tarea.getDependencias().clear();
            if (tareaActualizada.getDependencias() != null) {
                for (Dependencia dependencia : tareaActualizada.getDependencias()) {
                    Dependencia nuevaDependencia = new Dependencia();
                    nuevaDependencia.setTarea(tarea);
                    nuevaDependencia.setTareaDependiente(dependencia.getTareaDependiente());
                    tarea.getDependencias().add(nuevaDependencia);
                }
            }

            // Guardar cambios
            tareaRepository.save(tarea);
            return ResponseEntity.ok(tarea);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la tarea: " + e.getMessage());
        }
    }

    // Método para verificar si ya existe una dependencia entre dos tareas
    private boolean existeDependencia(Long tareaId, Long tareaDependienteId) {
        return dependenciaRepository.existsByTareaIdAndTareaDependienteId(tareaId, tareaDependienteId);
    }

    // Eliminar una tarea por ID, verificando primero si es dependencia de otra tarea
    public void eliminarTarea(Long id) {
        // Verificar si la tarea es una dependencia de otra tarea
        boolean esDependencia = dependenciaRepository.existsByTareaDependiente(new Tarea(id));

        if (esDependencia) {
            throw new IllegalStateException("No se puede eliminar la tarea porque es una dependencia de otra tarea.");
        }

        // Si no es dependencia, proceder a eliminar
        tareaRepository.deleteById(id);
    }

    // Obtener el plan de tareas optimizado desde Prolog
    public List<String> obtenerPlanOptimizado() {
        try {
            List<Tarea> tareas = obtenerTareas();
            List<String> tareasProlog = convertirTareasAProlog(tareas); // Ahora genera List<String>

            // Enviar las tareas al motor Prolog
            prologEngine.cargarTareasEnProlog(tareasProlog);

            String consultaProlog = "plan_optimo(Tareas, Plan)";
            Query query = new Query(consultaProlog);

            if (query.hasSolution()) {
                Term planTerm = query.oneSolution().get("Plan");
                return convertirPlanALista(planTerm);
            } else {
                throw new RuntimeException("No se pudo generar un plan optimizado.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el plan optimizado.", e);
        }
    }

    // Método auxiliar para convertir tareas a formato compatible con Prolog
    private List<String> convertirTareasAProlog(List<Tarea> tareas) {
        return tareas.stream().map(tarea -> {
            // Formatear nombre y prioridad para Prolog
            String nombre = tarea.getNombre().toLowerCase();
            String prioridad = tarea.getPrioridad().toString().toLowerCase();
            int duracion = Math.round(tarea.getTiempoEstimado());

            // Formatear dependencias como lista en formato Prolog
            String dependencias = tarea.getDependencias().stream()
                    .map(dep -> dep.getTareaDependiente().getNombre().toLowerCase())
                    .collect(Collectors.joining(",", "[", "]"));

            // Formatear condiciones climáticas como lista en formato Prolog
            String condicionesClimaticas = tarea.getCondicionesClimaticas().stream()
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .collect(Collectors.joining(",", "[", "]"));

            // Construir predicado Prolog
            return String.format("tarea(%s, %s, %d, %s, %s).", nombre, prioridad, duracion, dependencias, condicionesClimaticas);
        }).collect(Collectors.toList());
    }

    // Método para verificar que las dependencias especificadas existen en la base de datos
    private void validarDependencias(List<Dependencia> dependencias) {
        for (Dependencia dependencia : dependencias) {
            Long tareaDependienteId = dependencia.getTareaDependiente().getId();
            if (!tareaRepository.existsById(tareaDependienteId)) {
                throw new RuntimeException("Dependencia no válida: La tarea con ID " + tareaDependienteId + " no existe.");
            }
        }
    }

    // Método para obtener el plan si es posible
    public List<String> obtenerPlanSiPosible() {
        try {
            List<Tarea> tareas = obtenerTareas();
            List<String> tareasProlog = convertirTareasAProlog(tareas);

            // Enviar las tareas al motor Prolog
            prologEngine.cargarTareasEnProlog(tareasProlog);

            String consultaProlog = "plan_posible(Tareas, Plan)";
            Query query = new Query(consultaProlog);

            if (query.hasSolution()) {
                Term planTerm = query.oneSolution().get("Plan");
                return convertirPlanALista(planTerm);
            } else {
                return List.of("No se pudo generar un plan óptimo: condiciones climáticas o dependencias no satisfechas.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar si el plan es posible.", e);
        }
    }

    private List<String> convertirPlanALista(Term planTerm) {
        return List.of(planTerm.toTermArray()).stream()
                .map(Term::toString)
                .collect(Collectors.toList());
    }

    // Método para Actualizar el estado de una tarea en la base de datos y Generar un nuevo plan óptimo con las tareas pendientes.
    public List<String> actualizarEstadoYReplanificar(Long id, Estado nuevoEstado) {
        try {
            Tarea tarea = obtenerTareaPorId(id);
            tarea.setEstado(nuevoEstado);
            tareaRepository.save(tarea);

            // Replanificar con las tareas pendientes
            List<Tarea> tareasPendientes = obtenerTareasPorEstado(Estado.PENDIENTE);
            List<String> tareasProlog = convertirTareasAProlog(tareasPendientes);

            // Enviar las tareas al motor Prolog
            prologEngine.cargarTareasEnProlog(tareasProlog);

            String consultaProlog = "plan_optimo(TareasPendientes, Plan)";
            Query query = new Query(consultaProlog);

            if (query.hasSolution()) {
                Term planTerm = query.oneSolution().get("Plan");
                return convertirPlanALista(planTerm);
            } else {
                return List.of("No se pudo generar un nuevo plan debido a restricciones.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al replanificar tareas.", e);
        }
    }

    // Método para actualizar clima en Prolog y replanificar
    public List<String> replanificarSiCambiaClima(String nuevaCondicionClimatica) {
        // Actualizar la condición climática en Prolog
        prologEngine.actualizarClimaEnProlog(nuevaCondicionClimatica);

        // Generar el nuevo plan con las tareas pendientes
        return obtenerPlanSiPosible();
    }
}
