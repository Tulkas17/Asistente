package cr.ac.una.springbootaopmaven.controller;

import org.jpl7.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.TareaService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    // Endpoint para obtener el plan optimizado de tareas
    @GetMapping("/plan-optimizado")
    public List<Map<String, Term>> obtenerPlanOptimizado() {
        return tareaService.obtenerPlanOptimizado();
    }
}
