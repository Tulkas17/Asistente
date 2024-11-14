import React from 'react';

function TaskItem({ task }) {
    return (
        <li>
            <h3>{task.nombre}</h3>
            <p>Prioridad: {task.prioridad}</p>
            <p>Tiempo estimado: {task.tiempoEstimado} horas</p>
            <p>Fecha límite: {new Date(task.fechaLimite).toLocaleString()}</p>
        </li>
    );
}

export default TaskItem;
