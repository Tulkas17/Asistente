% src/main/resources/prolog/tareas.pl

% Ejemplo de reglas de tareas con dependencias y prioridades

tarea(comprar_comida, alta, 60, depende_de(clima)).
tarea(limpiar_casa, media, 120, independiente).
tarea(informe, alta, 180, depende_de(llamada)).

% Regla para verificar la completitud de las tareas
completar_tareas(Tareas, TiempoDisponible) :-
    % Verificar si todas las tareas pueden realizarse en el tiempo dado
    % y si se cumplen las dependencias
    verificar_dependencias(Tareas),
    calcular_tiempo(Tareas, Tiempo),
    Tiempo =< TiempoDisponible.
