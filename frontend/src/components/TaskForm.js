import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { toast } from 'react-toastify';
import { createTask, updateTask, getTasks } from '../services/api';

const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
`;

const FormContainer = styled.div`
  background: #ffffff;
  padding: 2.5rem;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 500px;
  max-height: 80vh;
  width: 100%;
  overflow-y: auto;
  position: relative;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 1.5rem;
  gap: 1rem;
`;

const SubmitButton = styled.button`
  flex: 1;
  background-color: ${({ theme }) => theme.colors.primary};
  color: white;
  padding: 0.75rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  &:hover {
    background-color: ${({ theme }) => theme.colors.secondary};
  }
`;

const CancelButton = styled(SubmitButton)`
  background-color: ${({ theme }) => theme.colors.danger};
  &:hover {
    background-color: #ff5a5a;
  }
`;

const StyledInput = styled.input`
  width: 100%;
  padding: 0.5rem;
  margin-top: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

const TaskForm = ({ closeModal, addTask, taskToEdit }) => {
  const [taskData, setTaskData] = useState({
    nombre: '',
    prioridad: 'ALTA',
    tiempoEstimado: '',
    fechaLimite: '',
    requisitos: '',
    condicionesClimaticas: [],
    dependencias: [],
    estado: 'PENDIENTE',
  });

  const [availableTasks, setAvailableTasks] = useState([]);
  const condicionesClimaticasOptions = ["SOLEADO", "NUBLADO", "LLUVIOSO", "VENTOSO", "INDEPENDIENTE"];

  useEffect(() => {
    fetchAvailableTasks();
    if (taskToEdit) {
      setTaskData({
        ...taskToEdit,
        fechaLimite: taskToEdit.fechaLimite ? new Date(taskToEdit.fechaLimite).toISOString().slice(0, 16) : '',
        dependencias: taskToEdit.dependencies ? taskToEdit.dependencies.map(dep => dep.tareaDependiente.id) : [],
        condicionesClimaticas: taskToEdit.condicionesClimaticas || [],
      });
    }
  }, [taskToEdit]);

  const fetchAvailableTasks = async () => {
    try {
      const response = await getTasks();
      const tasksWithoutCurrent = response.data.filter(task => taskToEdit ? task.id !== taskToEdit.id : true);
      setAvailableTasks(tasksWithoutCurrent);
    } catch (error) {
      console.error("Error al obtener tareas:", error);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setTaskData({ ...taskData, [name]: value });
  };

  const handleDependencyChange = (taskId) => {
    setTaskData((prevData) => {
      const isChecked = prevData.dependencias.includes(taskId);
      return {
        ...prevData,
        dependencias: isChecked
          ? prevData.dependencias.filter((id) => id !== taskId)
          : [...prevData.dependencias, taskId],
      };
    });
  };

  const handleClimaChange = (clima) => {
    setTaskData((prevData) => {
      const isChecked = prevData.condicionesClimaticas.includes(clima);
      return {
        ...prevData,
        condicionesClimaticas: isChecked
          ? prevData.condicionesClimaticas.filter((c) => c !== clima)
          : [...prevData.condicionesClimaticas, clima],
      };
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const fechaLimite = taskData.fechaLimite ? new Date(taskData.fechaLimite).toISOString() : null;
  
      // Crear el objeto con solo los campos necesarios
      const taskDataWithDependencies = {
        id: taskToEdit ? taskToEdit.id : null,
        nombre: taskData.nombre,
        prioridad: taskData.prioridad,
        tiempoEstimado: taskData.tiempoEstimado,
        fechaLimite,
        requisitos: taskData.requisitos,
        estado: taskData.estado,
        condicionesClimaticas: taskData.condicionesClimaticas,
        dependencias: taskData.dependencias.map((depId) => ({
          tarea: { id: taskToEdit ? taskToEdit.id : null },
          tareaDependiente: { id: depId },
        })),
      };
  
      console.log("Datos enviados al backend:", JSON.stringify(taskDataWithDependencies, null, 2));
  
      if (taskToEdit) {
        await updateTask(taskToEdit.id, taskDataWithDependencies);
        toast.success("Tarea actualizada exitosamente");
      } else {
        await createTask(taskDataWithDependencies);
        toast.success("Tarea guardada exitosamente");
      }
  
      addTask();
      closeModal();
    } catch (error) {
      toast.error("Error al guardar la tarea");
      console.error("Error:", error);
    }
  };  

  return (
    <ModalOverlay>
      <FormContainer>
        <h4>{taskToEdit ? "Editar Tarea" : "Añadir Nueva Tarea"}</h4>
        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label htmlFor="nombre">Nombre de la Tarea</label>
            <StyledInput type="text" id="nombre" name="nombre" value={taskData.nombre} onChange={handleChange} required />
          </div>
          <div className="mb-3">
            <label htmlFor="prioridad">Prioridad</label>
            <StyledInput as="select" id="prioridad" name="prioridad" value={taskData.prioridad} onChange={handleChange} required>
              <option value="ALTA">Alta</option>
              <option value="MEDIA">Media</option>
              <option value="BAJA">Baja</option>
            </StyledInput>
          </div>
          <div className="mb-3">
            <label htmlFor="tiempoEstimado">Tiempo Estimado (horas)</label>
            <StyledInput type="number" id="tiempoEstimado" name="tiempoEstimado" value={taskData.tiempoEstimado} onChange={handleChange} required />
          </div>
          <div className="mb-3">
            <label htmlFor="fechaLimite">Fecha y Hora Límite</label>
            <StyledInput type="datetime-local" id="fechaLimite" name="fechaLimite" value={taskData.fechaLimite} onChange={handleChange} required />
          </div>
          <div className="mb-3">
            <label htmlFor="requisitos">Requisitos Específicos</label>
            <StyledInput as="textarea" id="requisitos" name="requisitos" value={taskData.requisitos} onChange={handleChange} rows="3" />
          </div>
          <div className="mb-3">
            <label>Dependencias</label>
            <div style={{ maxHeight: '150px', overflowY: 'auto', border: '1px solid #ccc', padding: '0.5rem', borderRadius: '4px', marginBottom: '1rem' }}>
              {availableTasks.map((task) => (
                <div key={task.id} style={{ display: 'flex', alignItems: 'center', marginBottom: '0.5rem' }}>
                  <input
                    type="checkbox"
                    id={`task-${task.id}`}
                    checked={taskData.dependencias.includes(task.id)}
                    onChange={() => handleDependencyChange(task.id)}
                  />
                  <label htmlFor={`task-${task.id}`} style={{ marginLeft: '0.5rem' }}>{task.nombre}</label>
                </div>
              ))}
            </div>
          </div>
          <div className="mb-3">
            <label>Condiciones Climáticas</label>
            <div style={{ maxHeight: '150px', overflowY: 'auto', border: '1px solid #ccc', padding: '0.5rem', borderRadius: '4px', marginBottom: '1rem' }}>
              {condicionesClimaticasOptions.map((clima) => (
                <div key={clima} style={{ display: 'flex', alignItems: 'center', marginBottom: '0.5rem' }}>
                  <input
                    type="checkbox"
                    id={`clima-${clima}`}
                    checked={taskData.condicionesClimaticas.includes(clima)}
                    onChange={() => handleClimaChange(clima)}
                  />
                  <label htmlFor={`clima-${clima}`} style={{ marginLeft: '0.5rem' }}>{clima}</label>
                </div>
              ))}
            </div>
          </div>
          <ButtonContainer>
            <CancelButton type="button" onClick={closeModal}>Cancelar</CancelButton>
            <SubmitButton type="submit">{taskToEdit ? "Actualizar" : "Agregar"}</SubmitButton>
          </ButtonContainer>
        </form>
      </FormContainer>
    </ModalOverlay>
  );
};

export default TaskForm;
