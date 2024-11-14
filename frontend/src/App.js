import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { ThemeProvider } from 'styled-components';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import TaskForm from './components/TaskForm';
import ClimaPage from './pages/ClimaPage';
import Home from './pages/Home';
import theme from './theme';

function App() {
  return (
    <ThemeProvider theme={theme}>
      <Router>
        <div style={{ backgroundColor: theme.colors.background, minHeight: '100vh', display: 'flex', flexDirection: 'column', alignItems: 'center', padding: '2rem' }}>
          <h1 style={{ color: theme.colors.text }}>Asistente de Gestión de Tareas</h1>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/add" element={<TaskForm />} />
            <Route path="/clima" element={<ClimaPage />} />
          </Routes>
        </div>
        <ToastContainer position="top-right" autoClose={3000} hideProgressBar />
      </Router>
    </ThemeProvider>
  );
}

export default App;
