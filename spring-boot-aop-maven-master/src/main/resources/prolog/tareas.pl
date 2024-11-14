% Define los tipos de tareas y sus prioridades.
tarea(comprar_comida, alta, 60, depende_de(clima_soleado)).
tarea(limpiar_casa, media, 120, independiente).
tarea(informe, alta, 180, depende_de(llamada_cliente)).

% Reglas para determinar el orden de tareas
plan_optimo(Tareas, Plan) :-
    verificar_dependencias(Tareas),
    ordenar_por_prioridad(Tareas, Plan).

% Verifica dependencias
verificar_dependencias([]).
verificar_dependencias([Tarea | Resto]) :-
    % Verifica si una tarea depende de otra.
    depende(Tarea),
    verificar_dependencias(Resto).

% Ordena las tareas por prioridad (alta primero)
ordenar_por_prioridad(Tareas, Plan) :-
    % Aquí ordenas Tareas según la prioridad y las asignas a Plan
    sort(Tareas, Plan).

% Ejemplo de dependencias (esto debería basarse en datos dinámicos)
depende(Tarea) :-
    Tarea = tarea(_, _, _, depende_de(Condicion)),
    verificar_condicion(Condicion).

% Verifica condiciones de dependencias (ej. clima)
verificar_condicion(clima_soleado) :-
    % Aquí agregarías una regla para verificar el clima en un día específico
    clima(soleado).

% Ejemplo de condición de clima
clima(soleado).
