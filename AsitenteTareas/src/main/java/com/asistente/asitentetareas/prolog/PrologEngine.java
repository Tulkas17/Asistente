package com.asistente.asitentetareas.prolog;

import org.jpl7.Query;
import org.jpl7.Term;
import java.util.Map;

public class PrologEngine {

    public PrologEngine() {
        Query q1 = new Query("consult('src/main/resources/prolog/tareas.pl')");
        System.out.println("Consulting tareas.pl: " + (q1.hasSolution() ? "succeeded" : "failed"));
    }

    public Map<String, Term> realizarConsulta(String consulta) {
        Query query = new Query(consulta);
        return query.oneSolution(); // Devuelve el mapa completo de resultados
    }
}
