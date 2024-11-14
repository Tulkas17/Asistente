import React from 'react';
import TaskList from '../components/TaskList';
import ClimaInfo from '../components/ClimaInfo';

function Home() {
    return (
        <div style={{ width: '100%', maxWidth: '1200px', display: 'flex', flexDirection: 'column', gap: '2rem', margin: '0 auto' }}>
            <ClimaInfo />
            <TaskList />
        </div>
    );
}

export default Home;
