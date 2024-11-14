package cr.ac.una.springbootaopmaven.prolog;

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

        // Ejecutar una consulta de ejemplo en Prolog
        String consultaEjemplo = "tarea(comprar_comida, Prioridad, Duracion, Dependencia)";
        Query consulta = new Query(consultaEjemplo);

        // Verificar si la consulta tiene solución y mostrar los resultados
        if (consulta.hasSolution()) {
            Map<String, Term> resultado = consulta.oneSolution();
            System.out.println("Consulta ejecutada correctamente. Resultado:");
            resultado.forEach((variable, valor) -> System.out.println(variable + " = " + valor));
        } else {
            System.out.println("La consulta no tiene soluciones.");
        }
    }
}
