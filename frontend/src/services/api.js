import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: { 'Content-Type': 'application/json' }
});

// Funciones de API
export const getTasks = () => api.get('/tarea');
export const createTask = (taskData) => api.post('/tarea', taskData);
export const updateTask = (taskId, taskData) => api.put(`/tarea/${taskId}`, taskData);
export const getClima = () => api.get('/clima/actual');
export const getClimaHistorial = (start, end) => api.get('/clima/historial', { params: { start, end } });
export const registerClima = (climaData) => api.post('/clima/registrar', climaData);
export const getOptimalPlan = () => api.get('/plan/optimo');
export const updateTaskStatus = (taskId, newStatus) =>
    api.put(`/tarea/${taskId}/estado`, null, { params: { nuevoEstado: newStatus } });
export const deleteTask = (taskId) => api.delete(`/tarea/${taskId}`);
export const getDependencies = (taskId) => api.get(`/dependencia/por-tarea/${taskId}`);

export default api;