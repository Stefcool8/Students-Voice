import React, { useEffect, useState } from "react";
import apiClient from "../api/ApiClient.ts";
import { EvaluationTeacher } from "../types/EvaluationTeacher";
import "../styles/EvaluationsTeacher.css";

const EvaluationsTeacher: React.FC = () => {
    const [evaluations, setEvaluations] = useState<EvaluationTeacher[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    // Fetch teacher evaluations
    useEffect(() => {
        const fetchEvaluations = async () => {
            try {
                setLoading(true);
                const response = await apiClient.get<EvaluationTeacher[]>("/evaluations/teacher");
                console.log("Fetched teacher evaluations:", response.data);
                setEvaluations(response.data);
            } catch (err) {
                console.error("Error fetching teacher evaluations:", err);
                setError("Failed to fetch teacher evaluations.");
            } finally {
                setLoading(false);
            }
        };

        fetchEvaluations().then();
    }, []);

    if (loading) {
        return <div>Loading evaluations...</div>;
    }

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div className="evaluations-teacher">
            <h1>Your Evaluations</h1>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Activity Name</th>
                    <th>Type</th>
                    <th>Grade</th>
                    <th>Comment</th>
                    <th>Timestamp</th>
                </tr>
                </thead>
                <tbody>
                {evaluations.map((evaluation) => (
                    <tr key={evaluation.id}>
                        <td>{evaluation.id}</td>
                        <td>{evaluation.activityName}</td>
                        <td>{evaluation.activityType}</td>
                        <td>{evaluation.grade}</td>
                        <td>{evaluation.comment}</td>
                        <td>{evaluation.timestamp}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default EvaluationsTeacher;
