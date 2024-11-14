package cr.ac.una.backend.prolog;

import org.jpl7.Query;
import org.jpl7.Term;

import java.util.Map;

public class TestPrologConnection {

    public static void main(String[] args) {
        // Cargar el archivo Prolog
        String consultQuery = "consult('src/main/resources/prolog/tareas.pl')";
        Query query = new Query(consultQuery);

        // Verificar si el archivo Prolog se cargó exitosamente
        if (query.hasSolution()) {
            System.out.println("Archivo Prolog cargado correctamente.");
        } else {
            System.out.println("Error al cargar el archivo Prolog.");
            return;
        }

        // Definir el nombre de la tarea que queremos consultar
        String nombreTarea = "jardineria";
        System.out.println("Consultando información sobre la tarea: " + nombreTarea);

        // Ejecutar una consulta de ejemplo para obtener información sobre una tarea específica
        String consultaEjemplo = "tarea(" + nombreTarea + ", Prioridad, Duracion, Dependencia, CondicionClimatica)";
        Query consulta = new Query(consultaEjemplo);

        // Verificar si la consulta tiene solución y mostrar los resultados
        if (consulta.hasSolution()) {
            Map<String, Term> resultado = consulta.oneSolution();
            System.out.println("Consulta ejecutada correctamente. Resultado:");
            resultado.forEach((variable, valor) -> System.out.println(variable + " = " + valor));
        } else {
            System.out.println("La consulta no tiene soluciones.");
        }

        // Realizar una consulta para obtener el plan óptimo de tareas
        String ListaPlanes = "reparar_auto, informe, limpiar_casa, comprar_comida";
        System.out.println("Generando plan óptimo de tareas: " + ListaPlanes);
        String consultaPlan = "plan_optimo(["+ListaPlanes+"], Plan)";
        Query consultaPlanOptimo = new Query(consultaPlan);

        // Verificar si el plan tiene solución y mostrar el resultado
        if (consultaPlanOptimo.hasSolution()) {
            Map<String, Term> planResultado = consultaPlanOptimo.oneSolution();
            System.out.println("Plan óptimo de tareas:");
            planResultado.forEach((variable, valor) -> System.out.println(variable + " = " + valor));
        } else {
            System.out.println("No se pudo generar un plan óptimo.");
        }
    }
}
