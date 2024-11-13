// src/main/java/com/tuempresa/asistente_tareas/controller/TareaController.java
package com.asistente.asitentetareas.controller;

import com.asistente.asitentetareas.model.Tarea;
import com.asistente.asitentetareas.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {
    @Autowired
    private TareaService tareaService;

    @GetMapping
    public List<Tarea> obtenerTareas() {
        return tareaService.obtenerTareas();
    }

    @PostMapping
    public Tarea crearTarea(@RequestBody Tarea tarea) {
        return tareaService.guardarTarea(tarea);
    }

    @DeleteMapping("/{id}")
    public void eliminarTarea(@PathVariable Long id) {
        tareaService.eliminarTarea(id);
    }
}
