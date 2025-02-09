import React, { useEffect, useState } from "react";
import apiClient from "../api/ApiClient.ts";
import { EvaluationStudentResponse } from "../types/EvaluationStudentResponse.ts";
import "../styles/EvaluationsStudent.css";

const EvaluationsStudent: React.FC = () => {
    const [evaluations, setEvaluations] = useState<EvaluationStudentResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    // Fetch student evaluations
    useEffect(() => {
        const fetchEvaluations = async () => {
            try {
                setLoading(true);
                const response = await apiClient.get<EvaluationStudentResponse[]>("/evaluations/student");
                console.log("Fetched student evaluations:", response.data);
                setEvaluations(response.data);
            } catch (err) {
                console.error("Error fetching student evaluations:", err);
                setError("Failed to fetch student evaluations.");
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
        <div className="evaluations-student">
            <h1>Student Evaluations</h1>
            <table>
                <thead>
                <tr>
                    <th>Registration Number</th>
                    <th>Teacher</th>
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
                        <td>{evaluation.teacherName}</td>
                        <td>{evaluation.activityName}</td>
                        <td>{evaluation.activityType}</td>
                        <td>{evaluation.grade}</td>
                        <td>{evaluation.comment}</td>
                        <td>{new Date(evaluation.timestamp).toLocaleString()}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default EvaluationsStudent;
