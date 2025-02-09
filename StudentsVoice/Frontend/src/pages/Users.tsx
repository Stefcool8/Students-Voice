import React, { useEffect, useState } from 'react';
import apiClient from '../api/ApiClient.ts';
import '../styles/Users.css';

interface User {
    id: number;
    username: string;
    email: string;
    name: string;
    password: string;
    role: string;
    enabled: boolean;
}

interface Role {
    code: string;
    name: string;
}

const Users: React.FC = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [roles, setRoles] = useState<Role[]>([]);
    const [newUser, setNewUser] = useState({
        username: '',
        email: '',
        name: '',
        password: '',
        role: '',
        enabled: true,
    });
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    // Fetch all data
    const fetchData = async () => {
        try {
            setLoading(true);
            setError(null);

            // Fetch users
            const usersResponse = await apiClient.get<User[]>('/users');
            setUsers(usersResponse.data);

            // Fetch roles
            const rolesResponse = await apiClient.get<Role[]>('/roles');
            setRoles(rolesResponse.data);
        } catch (err) {
            console.error('Error fetching data:', err);
            setError('Failed to fetch data.');
        } finally {
            setLoading(false);
        }
    };

    // Add new user
    const handleAddUser = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            setError(null);

            await apiClient.post('/users', {
                ...newUser,
                role: roles.find(role => role.code === newUser.role)?.code,
            });
            setNewUser({ username: '', email: '', name: '', password: '', role: '', enabled: true });
            await fetchData();
        } catch (err) {
            console.error('Error adding user:', err);
            setError('Failed to add user.');
        }
    };

    // Delete user
    const handleDelete = async (id: number) => {
        try {
            setError(null);

            await apiClient.delete(`/users/${id}`);
            await fetchData();
        } catch (err) {
            console.error('Error deleting user:', err);
            setError('Failed to delete user.');
        }
    };

    useEffect(() => {
        fetchData().then();
    }, []);

    if (loading) return <p>Loading...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div className="users-page">
            <h1>Manage Users</h1>

            {/* Users Table */}
            <table className="users-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Name</th>
                    <th>Role</th>
                    <th>Enabled</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {users.map(user => (
                    <tr key={user.id}>
                        <td>{user.id}</td>
                        <td>{user.username}</td>
                        <td>{user.email}</td>
                        <td>{user.name}</td>
                        <td>{user.role}</td>
                        <td>{user.enabled ? 'Yes' : 'No'}</td>
                        <td>
                            <button onClick={() => handleDelete(user.id)}>Delete</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            {/* Add User Form */}
            <div className="add-user-form">
                <h2>Add User</h2>
                <form onSubmit={handleAddUser}>
                    <input
                        type="text"
                        placeholder="Username"
                        value={newUser.username}
                        onChange={e => setNewUser({ ...newUser, username: e.target.value })}
                        required
                    />
                    <input
                        type="email"
                        placeholder="Email"
                        value={newUser.email}
                        onChange={e => setNewUser({ ...newUser, email: e.target.value })}
                        required
                    />
                    <input
                        type="text"
                        placeholder="Name"
                        value={newUser.name}
                        onChange={e => setNewUser({ ...newUser, name: e.target.value })}
                        required
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={newUser.password}
                        onChange={e => setNewUser({ ...newUser, password: e.target.value })}
                        required
                    />
                    <select
                        value={newUser.role}
                        onChange={e => setNewUser({ ...newUser, role: e.target.value })}
                        required
                    >
                        <option value="">Select Role</option>
                        {roles.map(role => (
                            <option key={role.code} value={role.code}>
                                {role.name}
                            </option>
                        ))}
                    </select>
                    <label>
                        <input
                            type="checkbox"
                            checked={newUser.enabled}
                            onChange={() => setNewUser({ ...newUser, enabled: !newUser.enabled })}
                        />
                        Enabled
                    </label>
                    <button type="submit">Add User</button>
                </form>
            </div>
        </div>
    );
};

export default Users;
