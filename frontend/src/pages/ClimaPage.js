import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { getClima, getClimaHistorial } from '../services/api';

const ClimaContainer = styled.div`
  margin: 2rem;
  padding: 2rem;
  background: ${({ theme }) => theme.colors.light};
  border-radius: ${({ theme }) => theme.borderRadius};
`;

function ClimaPage() {
  const [clima, setClima] = useState(null);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [historial, setHistorial] = useState([]);

  const fetchClima = async () => {
    const response = await getClima();
    setClima(response.data);
  };

  const fetchHistorial = async () => {
    try {
      const response = await getClimaHistorial(startDate, endDate);
      setHistorial(response.data);
    } catch (error) {
      console.error("Error al obtener el historial de clima:", error);
    }
  };

  useEffect(() => {
    fetchClima();
  }, []);

  return (
    <ClimaContainer>
      <h2>Información del Clima</h2>
      <p>Condición actual: {clima?.tipo}</p>
      <input type="date" onChange={(e) => setStartDate(e.target.value)} />
      <input type="date" onChange={(e) => setEndDate(e.target.value)} />
      <button onClick={fetchHistorial}>Obtener Historial</button>
      {historial.map((h) => <p key={h.id}>{h.tipo} - {h.hora}</p>)}
    </ClimaContainer>
  );
}

export default ClimaPage;
