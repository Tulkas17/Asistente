package com.asistente.asistentetareas.prolog;


import org.jpl7.Query;

public class TestPrologConnection {

    public static void main(String[] args) {
        // Crear una instancia de PrologEngine para cargar el archivo Prolog
        PrologEngine prologEngine = new PrologEngine();

        // Realizar una consulta simple en Prolog
        String consulta = "tarea(comprar_comida, Prioridad, Duracion, Dependencia)";
        Query query = new Query(consulta);

        // Verificar si la consulta tiene una solución
        if (query.hasSolution()) {
            System.out.println("Conexión con Prolog exitosa y consulta ejecutada correctamente.");
            System.out.println("Resultados:");
            System.out.println(query.oneSolution());
        } else {
            System.out.println("Conexión con Prolog fallida o consulta sin soluciones.");
        }
    }
}
