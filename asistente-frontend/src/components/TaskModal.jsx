// TaskModal.jsx
import React from 'react';

function TaskModal({ show, handleClose, handleSave }) {
  return (
    <div className={`modal fade ${show ? 'show d-block' : ''}`} tabIndex="-1" aria-labelledby="taskModalLabel" aria-hidden={!show}>
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title" id="taskModalLabel">Añadir Nueva Tarea</h5>
            <button type="button" className="btn-close" onClick={handleClose} aria-label="Close"></button>
          </div>
          <div className="modal-body">
            <form id="taskForm">
              <div className="mb-3">
                <label htmlFor="taskName" className="form-label">Nombre de la Tarea</label>
                <input type="text" className="form-control" id="taskName" required />
              </div>
              <div className="mb-3">
                <label htmlFor="priority" className="form-label">Prioridad</label>
                <select className="form-select" id="priority" required>
                  <option value="alta">Alta</option>
                  <option value="media">Media</option>
                  <option value="baja">Baja</option>
                </select>
              </div>
              <div className="mb-3">
                <label htmlFor="estimatedTime" className="form-label">Tiempo Estimado (horas)</label>
                <input type="number" className="form-control" id="estimatedTime" required />
              </div>
              <div className="mb-3">
                <label htmlFor="deadline" className="form-label">Fecha y Hora Limite</label>
                <input type="datetime-local" className="form-control" id="deadline" required />
              </div>
              <div className="mb-3">
                <label htmlFor="requirements" className="form-label">Requisitos Específicos</label>
                <textarea className="form-control" id="requirements" rows="3"></textarea>
              </div>
              <div className="mb-3">
                <label htmlFor="dependency" className="form-label">Dependencia de Tarea</label>
                <select className="form-select" id="dependency" defaultValue={'Sin dependencia'}>
                  <option value="">Sin dependencia</option>
                </select>
              </div>
              <button type="button" className="btn btn-primary" onClick={handleSave}>Guardar Tarea</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default TaskModal;
