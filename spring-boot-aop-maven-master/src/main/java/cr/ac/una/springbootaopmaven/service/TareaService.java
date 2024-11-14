package cr.ac.una.springbootaopmaven.service;

import cr.ac.una.springbootaopmaven.entity.Tarea;
import cr.ac.una.springbootaopmaven.repository.TareaRepository;
import org.jpl7.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cr.ac.una.springbootaopmaven.prolog.PrologEngine;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    private PrologEngine prologEngine = new PrologEngine();

    // Obtener todas las tareas
    public List<Tarea> obtenerTareas() {
        return tareaRepository.findAll();
    }

    // Obtener una tarea por ID
    public Tarea obtenerTareaPorId(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
    }

    // Crear una nueva tarea
    public Tarea crearTarea(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    // Actualizar una tarea existente
    public Tarea actualizarTarea(Long id, Tarea tareaActualizada) {
        return tareaRepository.findById(id).map(tarea -> {
            tarea.setNombre(tareaActualizada.getNombre());
            tarea.setPrioridad(tareaActualizada.getPrioridad());
            tarea.setTiempoEstimado(tareaActualizada.getTiempoEstimado());
            tarea.setDependencias(tareaActualizada.getDependencias());
            tarea.setCondicionesClimaticas(tareaActualizada.getCondicionesClimaticas());
            return tareaRepository.save(tarea);
        }).orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
    }

    // Eliminar una tarea por ID
    public void eliminarTarea(Long id) {
        tareaRepository.deleteById(id);
    }

    // Obtener el plan de tareas optimizado desde Prolog
    public List<String> obtenerPlanOptimizado() {
        List<Tarea> tareas = obtenerTareas();
        List<Map<String, Object>> tareasProlog = convertirTareasAProlog(tareas);
        List<Map<String, Term>> resultadoProlog = prologEngine.obtenerPlanDeTareas(tareasProlog);

        // Convertir los términos resultantes en strings para una fácil visualización o procesamiento
        return resultadoProlog.stream()
                .map(map -> map.get("Plan").toString()) // Convierte el Term a String directamente
                .collect(Collectors.toList());
    }


    // Método auxiliar para convertir tareas a formato compatible con Prolog
    private List<Map<String, Object>> convertirTareasAProlog(List<Tarea> tareas) {
        return tareas.stream().map(tarea -> {
            Map<String, Object> tareaMap = new HashMap<>();
            tareaMap.put("nombre", tarea.getNombre());
            tareaMap.put("prioridad", tarea.getPrioridad().toString());
            tareaMap.put("tiempoEstimado", tarea.getTiempoEstimado());
            tareaMap.put("dependencias", tarea.getDependencias().stream().map(dep -> dep.getTareaDependiente().getNombre()).collect(Collectors.toList()));
            tareaMap.put("condicionesClimaticas", tarea.getCondicionesClimaticas());
            return tareaMap;
        }).collect(Collectors.toList());
    }
}
