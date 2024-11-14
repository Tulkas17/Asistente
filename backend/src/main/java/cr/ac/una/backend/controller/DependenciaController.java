package cr.ac.una.backend.controller;

import cr.ac.una.backend.entity.Dependencia;
import cr.ac.una.backend.service.DependenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dependencia")
public class DependenciaController {

    @Autowired
    private DependenciaService dependenciaService;

    // Crear una nueva dependencia entre tareas
    @PostMapping("/crear")
    public ResponseEntity<String> crearDependencia(@RequestBody Dependencia dependencia) {
        try {
            dependenciaService.crearDependencia(dependencia);
            return ResponseEntity.ok("Dependencia creada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la dependencia.");
        }
    }

    // Obtener todas las dependencias de una tarea específica
    @GetMapping("/por-tarea/{id}")
    public List<Dependencia> obtenerDependenciasPorTarea(@PathVariable Long id) {
        return dependenciaService.obtenerDependenciasPorTarea(id);
    }

    // Obtener todas las tareas de las que depende una tarea específica
    @GetMapping("/por-tarea-dependiente/{id}")
    public List<Dependencia> obtenerDependenciasPorTareaDependiente(@PathVariable Long id) {
        return dependenciaService.obtenerDependenciasPorTareaDependiente(id);
    }
}
