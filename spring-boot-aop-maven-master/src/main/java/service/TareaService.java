package service;

import cr.ac.una.springbootaopmaven.entity.Tarea;

import java.util.List;

public interface TareaService {

    List<Tarea> obtenerTareas();

    Tarea obtenerTareaPorId(Long id);

    Tarea crearTarea(Tarea tarea);

    Tarea actualizarTarea(Long id, Tarea tareaActualizada);

    void eliminarTarea(Long id);

    List<String> obtenerPlanOptimizado();
}
