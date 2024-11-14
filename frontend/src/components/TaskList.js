import React, { useState, useEffect } from 'react';
import styled, { useTheme } from 'styled-components';
import { getTasks, deleteTask, getOptimalPlan, getDependencies } from '../services/api';
import TaskForm from './TaskForm';
import { toast } from 'react-toastify';

const TaskContainer = styled.div`
  width: 100%;
  max-width: 1000px;
  margin: 0 auto;
  padding: ${({ theme }) => theme.spacing.medium};
  background: ${({ theme }) => theme.colors.light};
  border-radius: ${({ theme }) => theme.borderRadius};
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
`;

const HeaderContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: ${({ theme }) => theme.spacing.large};
`;

const Title = styled.h3`
  flex: 1;
  text-align: center;
`;

const ButtonContainer = styled.div`
  display: flex;
  gap: 1rem;
`;

const TaskCard = styled.div`
  padding: ${({ theme }) => theme.spacing.medium};
  background: ${({ theme }) => theme.colors.light};
  border: 1px solid #e9ecef;
  border-radius: ${({ theme }) => theme.borderRadius};
  margin-bottom: ${({ theme }) => theme.spacing.small};
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;

const TaskHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: ${({ theme }) => theme.spacing.small};
`;

const TaskTitle = styled.strong`
  font-size: 1.1rem;
  color: ${({ theme }) => theme.colors.text};
`;

const ButtonGroup = styled.div`
  display: flex;
  gap: ${({ theme }) => theme.spacing.small};
`;

const DeleteButton = styled.button`
  background-color: ${({ theme }) => theme.colors.danger};
  border: none;
  border-radius: 50%;
  color: white;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  &:hover {
    background-color: #ff5a5a;
  }
`;

const EditButton = styled(DeleteButton)`
  background-color: ${({ theme }) => theme.colors.primary};
  &:hover {
    background-color: ${({ theme }) => theme.colors.secondary};
  }
`;

const TaskDetails = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
`;

const PriorityIndicator = styled.span`
  color: ${({ priority, theme }) => {
    switch (priority) {
      case 'ALTA':
        return theme.colors.danger;
      case 'MEDIA':
        return theme.colors.secondary;
      case 'BAJA':
        return theme.colors.success;
      default:
        return theme.colors.text;
    }
  }};
`;

const StatusIndicator = styled.span`
  color: ${({ status, theme }) => {
    switch (status) {
      case 'PENDIENTE':
        return theme.colors.primary;
      case 'EN_PROGRESO':
        return theme.colors.secondary;
      case 'COMPLETADA':
        return theme.colors.success;
      default:
        return theme.colors.text;
    }
  }};
`;

const TaskList = () => {
  const theme = useTheme();
  const [tasks, setTasks] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [taskToEdit, setTaskToEdit] = useState(null);

  const openModal = () => setShowModal(true);
  const closeModal = () => {
    setShowModal(false);
    setTaskToEdit(null);
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  const fetchTasks = async () => {
    try {
      const response = await getTasks();
      const tasksWithDependencies = await Promise.all(response.data.map(async (task) => {
        const dependenciesResponse = await getDependencies(task.id);
        return { 
          ...task, 
          dependencies: dependenciesResponse.data || [] 
        };
      }));
      setTasks(tasksWithDependencies);
    } catch (error) {
      console.error("Error al obtener tareas:", error);
      toast.error("Error al cargar las tareas");
    }
  };

  const handleDeleteTask = async (taskId) => {
    if (window.confirm("¿Estás seguro de que deseas eliminar esta tarea?")) {
      try {
        await deleteTask(taskId);
        setTasks(tasks.filter((task) => task.id !== taskId));
        toast.success("Tarea eliminada exitosamente");
      } catch (error) {
        console.error("Error al eliminar tarea:", error);
        toast.error("Error al eliminar la tarea");
      }
    }
  };

  const handleEditTask = (task) => {
    // Filtramos tareas disponibles para dependencias
    const availableTasksForDependency = tasks.filter(t => t.id !== task.id);
    setTaskToEdit({ ...task, availableTasksForDependency });
    openModal();
  };

  const handleOptimizePlan = async () => {
    try {
      const response = await getOptimalPlan();
      toast.success("Plan optimizado obtenido");
      console.log("Plan optimizado:", response.data);
    } catch (error) {
      toast.error("Error al optimizar el plan");
      console.error("Error al optimizar el plan:", error);
    }
  };

  const addTask = async () => {
    await fetchTasks();
  };

  return (
    <TaskContainer>
      <HeaderContainer>
        <ButtonContainer>
          <button className="btn btn-secondary" onClick={handleOptimizePlan}>
            Optimizar Plan
          </button>
        </ButtonContainer>
        <Title>Tareas Diarias</Title>
        <ButtonContainer>
          <button className="btn btn-primary" onClick={openModal}>
            <i className="bi bi-plus-lg"></i> Añadir Tarea
          </button>
        </ButtonContainer>
      </HeaderContainer>
      {tasks.map(task => (
        <TaskCard key={task.id}>
          <TaskHeader>
            <TaskTitle>{task.nombre}</TaskTitle>
            <ButtonGroup>
              <EditButton onClick={() => handleEditTask(task)}>
                <i className="bi bi-pencil"></i>
              </EditButton>
              <DeleteButton onClick={() => handleDeleteTask(task.id)}>
                <i className="bi bi-trash"></i>
              </DeleteButton>
            </ButtonGroup>
          </TaskHeader>

          <TaskDetails>
            <div>
              <PriorityIndicator priority={task.prioridad}>Prioridad: {task.prioridad}</PriorityIndicator>
              <br />
              <StatusIndicator status={task.estado}>Estado: {task.estado}</StatusIndicator>
            </div>
            <div>
              <span>Fecha límite: {new Date(task.fechaLimite).toLocaleString()}</span>
              <br />
              <span>Tiempo estimado: {task.tiempoEstimado} hrs</span>
            </div>
            <div>
              <span>Requisitos: {task.requisitos || 'Ninguno'}</span>
            </div>
            <div>
              <span>Condiciones climáticas: {task.condicionesClimaticas?.join(', ') || 'Ninguna'}</span>
            </div>
            <div>
              <span>
                Dependencias: {Array.isArray(task.dependencies) && task.dependencies.length > 0
                  ? task.dependencies.map(dep => dep.tareaDependiente?.nombre).join(', ')
                  : 'Ninguna'}
              </span>
            </div>
          </TaskDetails>
        </TaskCard>
      ))}
      {showModal && <TaskForm closeModal={closeModal} addTask={addTask} taskToEdit={taskToEdit} />}
    </TaskContainer>
  );
};

export default TaskList;
