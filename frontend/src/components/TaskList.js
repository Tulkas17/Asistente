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

const ConfirmModalOverlay = styled.div`
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

const ConfirmModalContainer = styled.div`
  background: white;
  padding: 2rem;
  border-radius: ${({ theme }) => theme.borderRadius};
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  text-align: center;
`;

const ConfirmButton = styled.button`
  background-color: ${({ theme }) => theme.colors.danger};
  color: white;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: ${({ theme }) => theme.borderRadius};
  cursor: pointer;
  &:hover {
    background-color: #ff5a5a;
  }
`;

const CancelButton = styled.button`
  background-color: ${({ theme }) => theme.colors.secondary};
  color: white;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: ${({ theme }) => theme.borderRadius};
  cursor: pointer;
  &:hover {
    background-color: #6c757d;
  }
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

const OptimizeButton = styled.button`
  background-color: ${({ theme }) => theme.colors.success}; /* Verde */
  color: white;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: ${({ theme }) => theme.borderRadius};
  cursor: pointer;
  &:hover {
    background-color: #218838; /* Verde oscuro */
  }
`;

const AddTaskButton = styled.button`
  background-color: #ff6347; /* Color sugerido: rojo tomate */
  color: white;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: ${({ theme }) => theme.borderRadius};
  cursor: pointer;
  &:hover {
    background-color: #e55347; /* Un rojo ligeramente más oscuro */
  }
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
  display: inline-flex;
  align-items: center;
`;

const PriorityLabel = styled.span`
  color: ${({ theme }) => theme.colors.text};
  margin-right: 4px;
`;

const PriorityValue = styled.span`
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
  display: inline-flex;
  align-items: center;
`;

const StatusLabel = styled.span`
  color: ${({ theme }) => theme.colors.text};
  margin-right: 4px;
`;

const StatusValue = styled.span`
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
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [taskToDelete, setTaskToDelete] = useState(null);

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

  const openConfirmModal = (taskId) => {
    setTaskToDelete(taskId);
    setShowConfirmModal(true);
  };

  const closeConfirmModal = () => {
    setTaskToDelete(null);
    setShowConfirmModal(false);
  };

  const confirmDeleteTask = async () => {
    try {
      await deleteTask(taskToDelete);
      setTasks(tasks.filter((task) => task.id !== taskToDelete));
      toast.success("Tarea eliminada exitosamente");
    } catch (error) {
      console.error("Error al eliminar tarea:", error);
      toast.error("Error al eliminar la tarea");
    }
    closeConfirmModal();
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
          <OptimizeButton onClick={handleOptimizePlan}>Optimizar Plan</OptimizeButton>
        </ButtonContainer>
        <Title>Tareas Diarias</Title>
        <ButtonContainer>
          <AddTaskButton onClick={openModal}>
            <i className="bi bi-plus-lg"></i> Añadir Tarea
          </AddTaskButton>
        </ButtonContainer>
      </HeaderContainer>

      {tasks.map((task) => (
        <TaskCard key={task.id}>
          <TaskHeader>
            <TaskTitle>{task.nombre}</TaskTitle>
            <ButtonGroup>
              <EditButton onClick={() => handleEditTask(task)}>
                <i className="bi bi-pencil"></i>
              </EditButton>
              <DeleteButton onClick={() => openConfirmModal(task.id)}>
                <i className="bi bi-trash"></i>
              </DeleteButton>
            </ButtonGroup>
          </TaskHeader>

          <TaskDetails>
            <div>
              <PriorityIndicator>
                <PriorityLabel>Prioridad:</PriorityLabel>
                <PriorityValue priority={task.prioridad}>{task.prioridad}</PriorityValue>
              </PriorityIndicator>
              <br />
              <StatusIndicator>
                <StatusLabel>Estado:</StatusLabel>
                <StatusValue status={task.estado}>{task.estado}</StatusValue>
              </StatusIndicator>
            </div>
            <div>
              <span>Fecha de inicio: {new Date(task.fechaInicio).toLocaleString()}</span>
              <br />
              <span>Fecha límite: {new Date(task.fechaLimite).toLocaleString()}</span>
              <br />
              <span>Tiempo estimado: {task.tiempoEstimado} hrs</span>
            </div>
            <div>
              <span>Condiciones climáticas: {task.condicionesClimaticas?.join(', ') || 'Ninguna'}</span>
            </div>
            <div>
              <span>
                Dependencias: {Array.isArray(task.dependencies) && task.dependencies.length > 0
                  ? task.dependencies.map((dep) => dep.tareaDependiente?.nombre).join(', ')
                  : 'Ninguna'}
              </span>
            </div>
          </TaskDetails>
        </TaskCard>
      ))}

      {showModal && <TaskForm closeModal={closeModal} addTask={addTask} taskToEdit={taskToEdit} />}

      {showConfirmModal && (
        <ConfirmModalOverlay>
          <ConfirmModalContainer>
            <p>¿Estás seguro de que deseas eliminar esta tarea?</p>
            <div>
              <ConfirmButton onClick={confirmDeleteTask}>Eliminar</ConfirmButton>
              <CancelButton onClick={closeConfirmModal}>Cancelar</CancelButton>
            </div>
          </ConfirmModalContainer>
        </ConfirmModalOverlay>
      )}
    </TaskContainer>
  );
};

export default TaskList;
