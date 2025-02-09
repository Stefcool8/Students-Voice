import React, { useEffect, useState } from "react";
import apiClient from "../api/ApiClient.ts";
import { EvaluationAdmin } from "../types/EvaluationAdmin";
import "../styles/EvaluationsAdmin.css";

const EvaluationsAdmin: React.FC = () => {
    const [evaluations, setEvaluations] = useState<EvaluationAdmin[]>([]);
    const [evaluationCount, setEvaluationCount] = useState<number>(0);
    const [averageGradeAll, setAverageGradeAll] = useState<number>(0);
    const [averageGradePerTeacher, setAverageGradePerTeacher] = useState<Record<string, number>>({});
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    // Fetch all data
    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);

                // Fetch evaluations
                const evaluationsResponse = await apiClient.get<EvaluationAdmin[]>("/evaluations");
                setEvaluations(evaluationsResponse.data);

                // Fetch evaluation count
                const countResponse = await apiClient.get<number>("/evaluations/count");
                setEvaluationCount(countResponse.data);

                // Fetch average grade of all evaluations
                const averageGradeResponse = await apiClient.get<number>("/evaluations/average-grade");
                setAverageGradeAll(averageGradeResponse.data);

                // Fetch average grade per teacher
                const averageGradeMapResponse = await apiClient.get<Record<string, number>>("/evaluations/average-grade-map");
                setAverageGradePerTeacher(averageGradeMapResponse.data);
            } catch (err) {
                console.error("Error fetching data:", err);
                setError("Failed to fetch data.");
            } finally {
                setLoading(false);
            }
        };

        fetchData().then();
    }, []);

    if (loading) return <p>Loading...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div className="evaluations-admin">
            <h1>Admin - Evaluations</h1>

            <div>
                <h2>Statistics</h2>
                <p><strong>Total Evaluations:</strong> {evaluationCount}</p>
                <p><strong>Average Grade (All):</strong> {averageGradeAll.toFixed(2)}</p>
                <h3>Average Grade per Teacher</h3>
                <ul>
                    {Object.entries(averageGradePerTeacher).map(([teacher, average]) => (
                        <li key={teacher}>
                            <strong>{teacher}:</strong> {average.toFixed(2)}
                        </li>
                    ))}
                </ul>
            </div>

            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Teacher</th>
                    <th>Student</th>
                    <th>Activity Name</th>
                    <th>Activity Type</th>
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
                        <td>{evaluation.studentName}</td>
                        <td>{evaluation.activityName}</td>
                        <td>{evaluation.activityType}</td>
                        <td>{evaluation.grade}</td>
                        <td>{evaluation.comment || "N/A"}</td>
                        <td>{new Date(evaluation.timestamp).toLocaleString()}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default EvaluationsAdmin;
