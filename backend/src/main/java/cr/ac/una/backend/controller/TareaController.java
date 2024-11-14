package cr.ac.una.backend.controller;

import cr.ac.una.backend.entity.Tarea;
import cr.ac.una.backend.prolog.PrologExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cr.ac.una.backend.service.TareaService;

import java.util.List;

@RestController
@RequestMapping("/api/tarea")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    // Obtener todas las tareas
    @GetMapping
    public List<Tarea> obtenerTareas() {
        try {
            return tareaService.obtenerTareas();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener tareas: " + e.getMessage(), e);
        }
    }

    // Obtener una tarea por ID
    @GetMapping("/{id}")
    public Tarea obtenerTareaPorId(@PathVariable Long id) {
        try {
            return tareaService.obtenerTareaPorId(id);
        } catch (RuntimeException e) {
            throw new RuntimeException("Tarea no encontrada con ID: " + id, e);
        }
    }

    // Crear una nueva tarea
    @PostMapping
    public Tarea crearTarea(@RequestBody Tarea tarea) {
        return tareaService.crearTarea(tarea);
    }

    // Actualizar una tarea existente
    @PutMapping("/{id}")
    public Tarea actualizarTarea(@PathVariable Long id, @RequestBody Tarea tareaActualizada) {
        return tareaService.actualizarTarea(id, tareaActualizada);
    }

    // Eliminar una tarea por ID
    @DeleteMapping("/{id}")
    public void eliminarTarea(@PathVariable Long id) {
        tareaService.eliminarTarea(id);
    }


    // Endpoint para obtener el plan optimizado de tareas
    @GetMapping("/plan-optimizado")
    public List<String> obtenerPlanOptimizado() {
        try {
            return tareaService.obtenerPlanOptimizado();
        } catch (PrologExecutionException e) {
            System.err.println("PrologExecutionException: " + e.getMessage());
            return List.of("Error en Prolog: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error desconocido al obtener el plan optimizado: " + e.getMessage());
            return List.of("Error desconocido al obtener el plan optimizado.");
        }
    }
}
