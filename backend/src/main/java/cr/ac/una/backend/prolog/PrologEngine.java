package cr.ac.una.backend.prolog;


import org.jpl7.Query;
import org.jpl7.Term;

import java.util.List;
import java.util.Map;

public class PrologEngine {

    // Constructor para cargar el archivo de reglas Prolog
    public PrologEngine() {
        try {
            String consultQuery = "consult('src/main/resources/prolog/tareas.pl')";
            Query q1 = new Query(consultQuery);
            if (q1.hasSolution()) {
                System.out.println("Archivo Prolog cargado correctamente.");
            } else {
                throw new PrologLoadException("Error: No se pudo cargar el archivo Prolog.");
            }
        } catch (Exception e) {
            throw new PrologLoadException("Excepción al cargar el archivo Prolog.", e);
        }
    }

    public void cargarTareasEnProlog(List<String> tareasProlog) {
        try {
            // Limpia las tareas existentes en Prolog
            String limpiarTareas = "retractall(tarea(_, _, _, _, _)).";
            Query limpiarQuery = new Query(limpiarTareas);
            limpiarQuery.hasSolution();

            // Carga las tareas en Prolog
            for (String tarea : tareasProlog) {
                Query insertarQuery = new Query(tarea);
                if (!insertarQuery.hasSolution()) {
                    throw new PrologUpdateException("Error al insertar tarea en Prolog: " + tarea);
                }
            }
        } catch (Exception e) {
            throw new PrologUpdateException("Error al cargar las tareas en Prolog.", e);
        }
    }

    // Método para actualizar el clima en Prolog
    public void actualizarClimaEnProlog(String condicionClimatica) {
        String consulta = "retractall(clima_actual(_)), assert(clima_actual(" + condicionClimatica.toLowerCase() + "))";
        Query query = new Query(consulta);
        if (!query.hasSolution()) {
            throw new PrologUpdateException("Error al actualizar el clima en Prolog");
        }
    }
}
