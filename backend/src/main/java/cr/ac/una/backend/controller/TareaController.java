package cr.ac.una.backend.controller;

import cr.ac.una.backend.entity.Tarea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cr.ac.una.backend.service.TareaService;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    // Obtener todas las tareas
    @GetMapping
    public List<Tarea> obtenerTareas() {
        return tareaService.obtenerTareas();
    }

    // Obtener una tarea por ID
    @GetMapping("/{id}")
    public Tarea obtenerTareaPorId(@PathVariable Long id) {
        return tareaService.obtenerTareaPorId(id);
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
        return tareaService.obtenerPlanOptimizado();
    }
}
