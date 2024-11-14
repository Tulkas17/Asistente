import React, { useEffect, useState } from 'react';
import Axios from 'axios';
import TaskModal from '../components/TaskModal';

function TasksPage() {
  const [showModal, setShowModal] = useState(false);

  const [task, setTask] = useState({
    nombre: '',
    prioridad: 'alta',
    duracion: '',
    fechaLimite: '',
    dependencia: ''
  });

  const handleInputChange = (e) => {
    const { id, value } = e.target;
    setTask((prevTask) => ({
      ...prevTask,
      [id]: value
    }));
  };

  const handleOpenModal = () => setShowModal(true);
  const handleCloseModal = () => setShowModal(false);

  const handleSaveTask = () => {
    const task = {
      nombre: document.getElementById('taskName').value,
      prioridad: document.getElementById('priority').value,
      duracion: document.getElementById('estimatedTime').value,
      fechaLimite: document.getElementById('deadline').value,
      dependencia: document.getElementById('dependency').value,
      requerimientos: document.getElementById('requirements').value
    };

    Axios.post('http://localhost:8080/api/tareas', task)
      .then((response) => {
        console.log('task:', task)
        console.log('Task saved successfully:', response.data);
        setShowModal(false); 
      })
      .catch((error) => {
        console.error('Error saving task:', error);
      });
  };

  const handleDeleteTask = (id) => {
    Axios.delete(`http://localhost:8080/api/tareas/${id}`)
      .then((response) => {
        console.log('Task deleted successfully:', response.data);
      })
      .catch((error) => {
        console.error('Error deleting task:', error);
      });
  };
  
  let taskList = [];

  taskList.push({
      nombre: 'Tarea 1',
      prioridad: 'alta',
      duracion: 2,
      fechaLimite: '2021-10-15T10:00',
      dependencia: '',
      requerimientos: 'Requerimiento 1'
  });

  taskList.push({ 
      nombre: 'Tarea 2',
      prioridad: 'media',
      duracion: 4,
      fechaLimite: '2021-10-15T12:00',
      dependencia: 'Tarea 1',
      requerimientos: 'Requerimiento 2'
  });

  useEffect(() => {
    Axios.get('http://localhost:8080/api/tareas')
      .then((response) => {
      console.log('Tasks:', response.data);
      
      taskList = response.data;

      })
      .catch((error) => {
      console.error('Error fetching tasks:', error);
      });
  }, []);
    

  return (
    <>
      <div className="container-fluid mt-5">
        <div className="row mb-4">
          <div className="col-8">
            <h1 className="text-start">Asistente de Gestión de Tareas</h1>
          </div>
          <div className="col-4 text-end">
            <button className="btn btn-primary" onClick={handleOpenModal}>
              Añadir Nueva Tarea
            </button>
          </div>
        </div>

        <div className="card">
          <div className="card-body">
            <h3 className="text-start text-secondary mb-3">Tareas Diarias</h3>
            <ul id="taskList" className="list-group">
              {taskList.map((task, index) => (
                <li key={index} className="list-group-item mb-2 text-start">
                  <div className="d-flex justify-content-between">
                    <h5 className="mb-1">{task.nombre}</h5>
                    <button className="btn btn-danger btn-sm" onClick={() => handleDeleteTask(task.id)}>
                      Eliminar
                    </button>
                  </div>
                  <p className="mb-1">Prioridad: {task.prioridad}</p>
                  <p className="mb-1">Duración: {task.duracion} horas</p>
                  <p className="mb-1">Fecha límite: {new Date(task.fechaLimite).toLocaleString()}</p>
                  <p className="mb-1">Dependencia: {task.dependencia}</p>
                  <p className="mb-1">Requerimientos: {task.requerimientos}</p>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </div>

      <TaskModal 
        show={showModal} 
        handleClose={handleCloseModal} 
        handleSave={handleSaveTask} 
        task={task} 
        handleInputChange={handleInputChange} 
      />
    </>
  );
}

export default TasksPage;
