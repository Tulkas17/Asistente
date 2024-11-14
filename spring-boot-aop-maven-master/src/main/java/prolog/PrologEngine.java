package prolog;


import org.jpl7.Query;
import org.jpl7.Term;
import java.util.List;
import java.util.Map;

public class PrologEngine {

    // Constructor para cargar el archivo de reglas Prolog
    public PrologEngine() {
        String consultQuery = "consult('src/main/resources/prolog/tareas.pl')";
        Query q1 = new Query(consultQuery);
        if (q1.hasSolution()) {
            System.out.println("Archivo Prolog cargado correctamente.");
        } else {
            System.out.println("Error al cargar el archivo Prolog.");
        }
    }

    // Método para realizar consultas en Prolog
    public Map<String, Term> realizarConsulta(String consulta) {
        Query query = new Query(consulta);
        return query.oneSolution();
    }

    // Método específico para obtener un plan de tareas optimizado
    public List<Map<String, Term>> obtenerPlanDeTareas(List<Map<String, Object>> tareas) {
        // Convertir la lista de tareas a una estructura que Prolog pueda procesar.
        // Aquí puedes adaptar según tu formato y el archivo Prolog.
        String consulta = "plan_optimo(Tareas, Plan)";
        Query query = new Query(consulta);
        return List.of(query.allSolutions());
    }
}
