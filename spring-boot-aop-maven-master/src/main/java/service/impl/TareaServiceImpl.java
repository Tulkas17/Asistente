package service.impl;

import cr.ac.una.springbootaopmaven.entity.Tarea;
import cr.ac.una.springbootaopmaven.repository.TareaRepository;
import org.jpl7.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prolog.PrologEngine;
import service.TareaService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TareaServiceImpl implements TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    private PrologEngine prologEngine = new PrologEngine();

    @Override
    public List<Tarea> obtenerTareas() {
        return tareaRepository.findAll();
    }

    @Override
    public Tarea obtenerTareaPorId(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
    }

    @Override
    public Tarea crearTarea(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    @Override
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

    @Override
    public void eliminarTarea(Long id) {
        tareaRepository.deleteById(id);
    }

    @Override
    public List<String> obtenerPlanOptimizado() {
        List<Tarea> tareas = obtenerTareas();
        List<Map<String, Object>> tareasProlog = convertirTareasAProlog(tareas);
        List<Map<String, Term>> resultadoProlog = prologEngine.obtenerPlanDeTareas(tareasProlog);

        return resultadoProlog.stream()
                .map(map -> map.get("Plan").toString())
                .collect(Collectors.toList());
    }

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
