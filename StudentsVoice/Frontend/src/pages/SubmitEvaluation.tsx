import React, { useEffect, useState } from 'react';
import apiClient from "../api/ApiClient.ts";
import { EvaluationStudentRequest } from "../types/EvaluationStudentRequest.ts";
import "../styles/SubmitEvaluation.css";

const SubmitEvaluation: React.FC = () => {
    const [teachers, setTeachers] = useState<string[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [newEvaluation, setNewEvaluation] = useState<EvaluationStudentRequest>({
        teacherName: '',
        activityName: '',
        activityType: '',
        grade: 0,
        comment: '',
    });

    // Fetch teachers
    useEffect(() => {
        const fetchTeachers = async () => {
            try {
                setLoading(true);
                const response = await apiClient.get<string[]>('users/teacher-names');
                console.log('Fetched teachers:', response.data);
                setTeachers(response.data);
            } catch (err) {
                console.error('Error fetching teachers:', err);
                setError('Failed to fetch teachers.');
            } finally {
                setLoading(false);
            }
        };

        fetchTeachers().then();
    }, []);

    if (loading) {
        return <div>Loading teachers...</div>;
    }

    if (error) {
        return <div>{error}</div>;
    }

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        // Validation checks before sending the request
        if (!newEvaluation.teacherName) {
            alert('Please select a teacher.');
            return;
        }
        if (!newEvaluation.activityName.trim() || newEvaluation.activityName.length > 50) {
            alert('Activity name must not be blank and should not exceed 50 characters.');
            return;
        }
        if (!newEvaluation.activityType || !['COURSE', 'SEMINAR'].includes(newEvaluation.activityType)) {
            alert('Activity type must be "COURSE" or "SEMINAR".');
            return;
        }
        if (newEvaluation.grade < 1 || newEvaluation.grade > 10) {
            alert('Grade must be between 1 and 10.');
            return;
        }
        if (newEvaluation.comment && newEvaluation.comment.length > 500) {
            alert('Comment cannot exceed 500 characters.');
            return;
        }

        apiClient
            .post('/evaluations', newEvaluation)
            .then(() => {
                alert('Evaluation submitted successfully.');
                setNewEvaluation({
                    teacherName: '',
                    activityName: '',
                    activityType: '',
                    grade: 0,
                    comment: '',
                });
                window.location.href = '/evaluations-student';
            })
            .catch((error) => {
                alert('Error submitting evaluation.');
                console.error(error);
            });
    };

    return (
        <div className="submit-evaluation">
            <h1>Submit Evaluation</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Teacher:</label>
                    <select
                        value={newEvaluation.teacherName}
                        onChange={(e) => setNewEvaluation({ ...newEvaluation, teacherName: e.target.value })}
                        required
                    >
                        <option value="">Select a teacher</option>
                        {teachers.map((teacher) => (
                            <option key={teacher} value={teacher}>
                                {teacher}
                            </option>
                        ))}
                    </select>
                </div>

                <div>
                    <label>Activity Name:</label>
                    <input
                        type="text"
                        value={newEvaluation.activityName}
                        onChange={(e) => setNewEvaluation({ ...newEvaluation, activityName: e.target.value })}
                        required
                        maxLength={50}
                    />
                </div>

                <div>
                    <label>Activity Type:</label>
                    <select
                        value={newEvaluation.activityType}
                        onChange={(e) => setNewEvaluation({ ...newEvaluation, activityType: e.target.value })}
                        required
                    >
                        <option value="">Select an activity type</option>
                        <option value="COURSE">Course</option>
                        <option value="SEMINAR">Seminar</option>
                    </select>
                </div>

                <div>
                    <label>Grade:</label>
                    <input
                        type="number"
                        value={newEvaluation.grade}
                        onChange={(e) => setNewEvaluation({ ...newEvaluation, grade: parseInt(e.target.value, 10) })}
                        required
                        min={1}
                        max={10}
                    />
                </div>

                <div>
                    <label>Comment:</label>
                    <textarea
                        value={newEvaluation.comment}
                        onChange={(e) => setNewEvaluation({ ...newEvaluation, comment: e.target.value })}
                        maxLength={500}
                    />
                </div>

                <button type="submit">Submit</button>
            </form>
        </div>
    );
};

export default SubmitEvaluation;
