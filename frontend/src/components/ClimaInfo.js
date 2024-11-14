import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { getClima } from '../services/api';

const ClimaContainer = styled.div`
  text-align: center;
  padding: 1.5rem;
  background: ${({ theme }) => theme.colors.light};
  border-radius: ${({ theme }) => theme.borderRadius};
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
`;

function ClimaInfo() {
    const [clima, setClima] = useState(null);

    useEffect(() => {
        fetchClima();
    }, []);

    const fetchClima = async () => {
        try {
            const response = await getClima();
            setClima(response.data);
        } catch (error) {
            console.error('Error al obtener el clima:', error);
        }
    };

    return (
        <ClimaContainer>
            <h3>Clima Actual</h3>
            {clima ? (
                <div>
                    <p>Condición: {clima.tipo}</p>
                    <p>Hora: {new Date(clima.hora).toLocaleString()}</p>
                </div>
            ) : (
                <p>Cargando clima...</p>
            )}
        </ClimaContainer>
    );
}

export default ClimaInfo;
