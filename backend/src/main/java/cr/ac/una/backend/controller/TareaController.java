package cr.ac.una.backend.controller;

import cr.ac.una.backend.entity.Dependencia;
import cr.ac.una.backend.entity.Tarea;
import cr.ac.una.backend.enumeration.Estado;
import cr.ac.una.backend.prolog.PrologExecutionException;
import cr.ac.una.backend.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    // Obtener tareas por estado
    @GetMapping("/estado")
    public List<Tarea> obtenerTareasPorEstado(@RequestParam Estado estado) {
        return tareaService.obtenerTareasPorEstado(estado);
    }

    // Crear una nueva tarea
    @PostMapping
    public ResponseEntity<?> crearTarea(@RequestBody @Valid Tarea tarea) {
        Tarea nuevaTarea = tareaService.crearTarea(tarea);
        return ResponseEntity.ok(nuevaTarea);
    }

    // Actualizar una tarea existente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTarea(@PathVariable Long id, @RequestBody @Valid Tarea tareaActualizada) {
        ResponseEntity<?> tareaActualizadaRespuesta = tareaService.actualizarTarea(id, tareaActualizada);
        return ResponseEntity.ok(tareaActualizadaRespuesta);
    }

    // Eliminar una tarea por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTarea(@PathVariable Long id) {
        try {
            tareaService.eliminarTarea(id);
            return ResponseEntity.ok("Tarea eliminada exitosamente.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la tarea: " + e.getMessage());
        }
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

    // Endpoint para actualizar el estado de una tarea y replanificar
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoYReplanificar(@PathVariable Long id, @RequestParam Estado nuevoEstado) {
        try {
            // Actualizar el estado de la tarea y obtener el plan replanificado
            List<String> nuevoPlan = tareaService.actualizarEstadoYReplanificar(id, nuevoEstado);

            return ResponseEntity.ok(nuevoPlan);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el estado y replanificar: " + e.getMessage());
        }
    }
}
