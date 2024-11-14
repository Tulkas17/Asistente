package cr.ac.una.backend.service;

import cr.ac.una.backend.entity.Dependencia;
import cr.ac.una.backend.entity.Tarea;
import cr.ac.una.backend.enumeration.Estado;
import cr.ac.una.backend.prolog.PrologEngine;
import cr.ac.una.backend.prolog.PrologExecutionException;
import cr.ac.una.backend.repository.DependenciaRepository;
import cr.ac.una.backend.repository.TareaRepository;
import org.jpl7.Query;
import org.jpl7.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        validarDependencias(tarea.getDependencias());
        return tareaRepository.save(tarea);
    }

    // Actualizar el estado de una tarea
    public void actualizarEstadoTarea(Long id, Estado nuevoEstado) {
        Tarea tarea = obtenerTareaPorId(id);
        tarea.setEstado(nuevoEstado);
        tareaRepository.save(tarea);
    }

    // Actualizar una tarea existente
    public Tarea actualizarTarea(Long id, Tarea tareaActualizada) {
        validarDependencias(tareaActualizada.getDependencias());
        return tareaRepository.findById(id).map(tarea -> {
            tarea.setNombre(tareaActualizada.getNombre());
            tarea.setPrioridad(tareaActualizada.getPrioridad());
            tarea.setTiempoEstimado(tareaActualizada.getTiempoEstimado());
            tarea.setDependencias(tareaActualizada.getDependencias());
            tarea.setCondicionesClimaticas(tareaActualizada.getCondicionesClimaticas());
            return tareaRepository.save(tarea);
        }).orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
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
            List<Map<String, Object>> tareasProlog = convertirTareasAProlog(tareas);
            List<Map<String, Term>> resultadoProlog = prologEngine.obtenerPlanDeTareas(tareasProlog);

            return resultadoProlog.stream()
                    .map(map -> map.get("Plan").toString())
                    .collect(Collectors.toList());
        } catch (PrologExecutionException e) {
            System.err.println("Error en Prolog al obtener el plan optimizado: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Error desconocido al procesar el plan optimizado: " + e.getMessage());
            throw new RuntimeException("Error desconocido al procesar el plan optimizado.", e);
        }
    }

    // Método auxiliar para convertir tareas a formato compatible con Prolog
    private List<Map<String, Object>> convertirTareasAProlog(List<Tarea> tareas) {
        return tareas.stream().map(tarea -> {
            Map<String, Object> tareaMap = new HashMap<>();
            tareaMap.put("nombre", tarea.getNombre());
            tareaMap.put("prioridad", tarea.getPrioridad().toString());
            tareaMap.put("tiempoEstimado", tarea.getTiempoEstimado());
            tareaMap.put("dependencias", tarea.getDependencias().stream()
                    .map(dep -> dep.getTareaDependiente().getNombre())
                    .collect(Collectors.toList()));
            tareaMap.put("condicionesClimaticas", tarea.getCondicionesClimaticas());
            return tareaMap;
        }).collect(Collectors.toList());
    }

    // Método para verificar que las dependencias especificadas existen en la base de datos
    private void validarDependencias(List<Dependencia> dependencias) {
        for (Dependencia dependencia : dependencias) {
            Tarea tareaDependiente = dependencia.getTareaDependiente();
            if (!tareaRepository.existsById(tareaDependiente.getId())) {
                throw new RuntimeException("Dependencia no válida: La tarea con ID " + tareaDependiente.getId() + " no existe.");
            }
        }
    }

    // Método para obtener el plan si es posible
    public List<String> obtenerPlanSiPosible() {
        List<Tarea> tareas = obtenerTareas();
        List<Map<String, Object>> tareasProlog = convertirTareasAProlog(tareas);

        try {
            String consultaProlog = "plan_posible(Tareas, Plan)";
            Query query = new Query(consultaProlog);

            if (query.hasSolution()) {
                Term planTerm = query.oneSolution().get("Plan");
                return convertirPlanALista(planTerm);
            } else {
                return List.of("No se pudo generar un plan óptimo: condiciones climáticas o dependencias no satisfechas.");
            }
        } catch (PrologExecutionException e) {
            System.err.println("Error en Prolog al verificar plan posible: " + e.getMessage());
            return List.of("Error en Prolog al verificar plan posible.");
        } catch (Exception e) {
            System.err.println("Error desconocido al verificar plan posible: " + e.getMessage());
            return List.of("Error desconocido al verificar plan posible.");
        }
    }

    private List<String> convertirPlanALista(Term planTerm) {
        return List.of(planTerm.toTermArray()).stream()
                .map(Term::toString)
                .collect(Collectors.toList());
    }

    // Método para Actualizar el estado de una tarea en la base de datos y Generar un nuevo plan óptimo con las tareas pendientes.
    public List<String> actualizarEstadoYReplanificar(Long id, Estado nuevoEstado) {
        Tarea tarea = obtenerTareaPorId(id);
        tarea.setEstado(nuevoEstado);
        tareaRepository.save(tarea);

        // Replanificar con las tareas restantes
        List<Tarea> tareasPendientes = obtenerTareasPorEstado(Estado.PENDIENTE);
        List<Map<String, Object>> tareasProlog = convertirTareasAProlog(tareasPendientes);

        try {
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
