package service;


import cr.ac.una.springbootaopmaven.entity.Tarea;
import cr.ac.una.springbootaopmaven.repository.TareaRepository;
import org.jpl7.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prolog.PrologEngine;

import java.util.List;
import java.util.Map;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    private PrologEngine prologEngine = new PrologEngine();

    // Obtener todas las tareas
    public List<Tarea> obtenerTareas() {
        return tareaRepository.findAll();
    }

    // Guardar una nueva tarea
    public Tarea guardarTarea(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    // Obtener el plan de tareas optimizado desde Prolog
    public List<Map<String, Term>> obtenerPlanOptimizado() {
        List<Tarea> tareas = obtenerTareas();
        // Convertir tareas a un formato que Prolog pueda interpretar
        // Aquí tendrías que mapear los datos de Java a Prolog
        List<Map<String, Object>> tareasProlog = convertirTareasAProlog(tareas);
        return prologEngine.obtenerPlanDeTareas(tareasProlog);
    }

    // Método auxiliar para convertir tareas a formato compatible con Prolog
    private List<Map<String, Object>> convertirTareasAProlog(List<Tarea> tareas) {
        // Convierte cada Tarea a un Map compatible con Prolog
        // Implementa según el formato que necesitas
        return null;
    }
}
