package cr.ac.una.backend.prolog;

import org.jpl7.Query;
import org.jpl7.Term;

import java.util.Map;

public class TestPrologConnection {

    public static void main(String[] args) {
        // Cargar el archivo Prolog
        String consultQuery = "consult('src/main/resources/prolog/tareas.pl')";
        Query query = new Query(consultQuery);

        if (query.hasSolution()) {
            System.out.println("Archivo Prolog cargado correctamente.");
        } else {
            System.out.println("Error al cargar el archivo Prolog.");
            return;
        }

        // Información sobre una tarea específica
        String nombreTarea = "'jardineria'";
        System.out.println("Consultando información sobre la tarea: " + nombreTarea);
        String consultaEjemplo = "tarea(" + nombreTarea + ", Prioridad, Duracion, Dependencia, CondicionClimatica)";
        Query consulta = new Query(consultaEjemplo);

        if (consulta.hasSolution()) {
            Map<String, Term> resultado = consulta.oneSolution();
            System.out.println("Resultado de la consulta:");
            resultado.forEach((k, v) -> System.out.println(k + " = " + v));
        } else {
            System.out.println("La consulta no tiene soluciones.");
        }

        // Generar plan óptimo
        String ListaPlanes = "'reparar_auto', 'informe', 'limpiar_casa', 'comprar_comida'";
        System.out.println("Generando plan óptimo de tareas: " + ListaPlanes);
        String consultaPlan = "plan_optimo([" + ListaPlanes + "], Plan)";
        Query consultaPlanOptimo = new Query(consultaPlan);

        if (consultaPlanOptimo.hasSolution()) {
            Map<String, Term> planResultado = consultaPlanOptimo.oneSolution();
            System.out.println("Plan óptimo de tareas:");
            planResultado.forEach((k, v) -> System.out.println(k + " = " + v));
        } else {
            System.out.println("No se pudo generar un plan óptimo.");
        }

        // Verificar condiciones climáticas
        String consultaCondiciones = "condiciones_climaticas_cumplidas('jardineria')";
        Query queryCondiciones = new Query(consultaCondiciones);
        System.out.println("¿Condiciones climáticas cumplidas para jardineria? " + (queryCondiciones.hasSolution() ? "Sí" : "No"));

        // Verificar si el plan es posible
        String consultaPlanPosible = "plan_posible(['reparar_auto', 'informe'], Plan)";
        Query queryPlanPosible = new Query(consultaPlanPosible);
        if (queryPlanPosible.hasSolution()) {
            Map<String, Term> resultadoPlanPosible = queryPlanPosible.oneSolution();
            System.out.println("Plan posible generado:");
            resultadoPlanPosible.forEach((k, v) -> System.out.println(k + " = " + v));
        } else {
            System.out.println("No se pudo generar un plan posible.");
        }
    }
}
