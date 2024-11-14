package cr.ac.una.backend.controller;

import org.jpl7.Query;
import org.jpl7.Term;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PlanController {

    // Obtener el plan óptimo de tareas
    @GetMapping("/plan-optimo")
    public List<String> obtenerPlanOptimo() {
        String consultaProlog = "plan_optimo([comprar_comida, limpiar_casa, informe, reparar_auto], Plan)";
        Query query = new Query(consultaProlog);

        if (query.hasSolution()) {
            Term planTerm = query.oneSolution().get("Plan");
            return convertirPlanALista(planTerm);
        } else {
            return List.of("No se pudo generar un plan óptimo.");
        }
    }

    private List<String> convertirPlanALista(Term planTerm) {
        List<String> plan = new ArrayList<>();
        for (Term tarea : planTerm.toTermArray()) {
            plan.add(tarea.toString());
        }
        return plan;
    }
}
