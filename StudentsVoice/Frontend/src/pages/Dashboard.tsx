import React from 'react';
import '../styles/Dashboard.css';
import {useUser} from "../context/UserContext.tsx";

const Dashboard: React.FC = () => {
    const { user } = useUser();

    return (
        <div className="dashboard-page">
            <h2>Welcome, {user?.sub}!</h2>
            <p>Current Evaluation Time Range: 2025-01-15 to 2025-02-15</p>
            <p>Evaluations are open!</p>
        </div>
    );
};

export default Dashboard;
