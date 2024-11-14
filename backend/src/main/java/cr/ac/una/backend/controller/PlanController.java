package cr.ac.una.backend.controller;

import cr.ac.una.backend.prolog.PrologExecutionException;
import cr.ac.una.backend.service.TareaService;
import org.jpl7.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/plan")
public class PlanController {

    @Autowired
    private TareaService tareaService;

    // Obtener el plan óptimo de tareas
    @GetMapping("/optimo")
    public List<String> obtenerPlanOptimo() {
        try {
            return tareaService.obtenerPlanOptimizado();
        } catch (PrologExecutionException e) {
            System.err.println("PrologExecutionException: " + e.getMessage());
            return List.of("Error en Prolog: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error desconocido al obtener el plan óptimo: " + e.getMessage());
            return List.of("Error desconocido al obtener el plan óptimo.");
        }
    }

    // Obtener el plan óptimo si es posible
    @GetMapping("/optimo/posible")
    public ResponseEntity<?> obtenerPlanSiPosible() {
        List<String> resultado = tareaService.obtenerPlanSiPosible();

        if (resultado.contains("No se pudo generar un plan óptimo")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No es posible generar un plan óptimo debido a condiciones climáticas o dependencias no satisfechas.");
        } else if (resultado.contains("Error en Prolog")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el plan en Prolog.");
        } else if (resultado.contains("Error desconocido")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error desconocido al verificar el plan.");
        }

        return ResponseEntity.ok(resultado);
    }

    private List<String> convertirPlanALista(Term planTerm) {
        List<String> plan = new ArrayList<>();
        for (Term tarea : planTerm.toTermArray()) {
            plan.add(tarea.toString());
        }
        return plan;
    }
}
