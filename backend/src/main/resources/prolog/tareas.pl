% Definición de tareas: tarea(Nombre, Prioridad, Duracion, Dependencias, CondicionesClimaticas).
tarea(comprar_comida, alta, 60, [], [soleado]).
tarea(limpiar_casa, media, 120, [comprar_comida], [independiente]).
tarea(informe, alta, 180, [], [independiente]).
tarea(reparar_auto, alta, 240, [], [soleado, nublado]).
tarea(plantar_flores, baja, 90, [comprar_comida], [soleado]).
tarea(reunion_equipo, alta, 60, [], [independiente]).
tarea(entrenamiento, media, 90, [reunion_equipo], [independiente]).
tarea(lavado_de_ropa, baja, 60, [], [nublado, lluvioso]).
tarea(construir_mueble, media, 150, [limpiar_casa], [independiente]).
tarea(jardineria, baja, 120, [plantar_flores], [soleado]).
tarea(estudio, alta, 180, [], [independiente]).
tarea(cita_medica, alta, 30, [], [independiente]).
tarea(paseo_perro, media, 45, [], [soleado, nublado]).
tarea(limpiar_auto, baja, 40, [lavado_de_ropa], [soleado]).
tarea(mantenimiento_jardin, media, 120, [jardineria], [soleado, nublado]).

% Prioridades de tareas
prioridad_mayor(alta, media).
prioridad_mayor(alta, baja).
prioridad_mayor(media, baja).

% Obtener todas las tareas de la base de conocimiento
todas_las_tareas(Tareas) :-
    findall(Tarea, tarea(Tarea, _, _, _, _), Tareas).

% Filtrar tareas según condiciones climáticas
tareas_cumplen_condiciones([], []).
tareas_cumplen_condiciones([Tarea|Resto], [Tarea|Filtradas]) :-
    condiciones_climaticas_cumplidas(Tarea),
    tareas_cumplen_condiciones(Resto, Filtradas).
tareas_cumplen_condiciones([_|Resto], Filtradas) :-
    tareas_cumplen_condiciones(Resto, Filtradas).

% Condiciones climáticas de tareas
condiciones_climaticas_cumplidas(Tarea) :-
    tarea(Tarea, _, _, _, Condiciones),
    verificar_condiciones_climaticas(Condiciones).

verificar_condiciones_climaticas([independiente]) :- !.
verificar_condiciones_climaticas(Condiciones) :-
    member(Condicion, Condiciones),
    clima_actual(Condicion),
    !.

% Estado actual del clima
clima_actual(soleado).
%clima_actual(nublado).
clima_actual(independiente).

% Agregar dependencias de forma recursiva para construir el conjunto completo de tareas necesarias
agregar_dependencias([], Todas, Todas).
agregar_dependencias([Tarea | Resto], Acumuladas, Todas) :-
    tarea(Tarea, _, _, Dependencias, _),
    union(Dependencias, Acumuladas, NuevasAcumuladas),
    agregar_dependencias(Dependencias, NuevasAcumuladas, AcumuladasConDependencias),
    agregar_dependencias(Resto, AcumuladasConDependencias, Todas).

% Ordenar tareas por prioridad
ordenar_por_prioridad(Tareas, TareasOrdenadas) :-
    predsort(comparar_prioridades, Tareas, TareasOrdenadas).

comparar_prioridades(Delta, Tarea1, Tarea2) :-
    tarea(Tarea1, Prioridad1, _, _, _),
    tarea(Tarea2, Prioridad2, _, _, _),
    (prioridad_mayor(Prioridad1, Prioridad2) -> Delta = '<' ; Delta = '>').

% Construir el plan óptimo considerando dependencias y condiciones climáticas
plan_optimo(Plan) :-
    findall(Tarea, (tarea(Tarea, _, _, _, _), condiciones_climaticas_cumplidas(Tarea)), Tareas),
    agregar_dependencias(Tareas, [], TodasTareas),
    ordenar_por_prioridad(TodasTareas, Plan),
    !.  % Este cut evita que Prolog siga generando soluciones adicionales

