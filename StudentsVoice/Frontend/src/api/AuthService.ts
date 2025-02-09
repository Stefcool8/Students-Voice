import apiClient from './ApiClient.ts';
import {jwtDecode} from 'jwt-decode';
import {User} from "../types/User.ts";

export const login = async (username: string, password: string) => {
    try {
        const response = await apiClient.post('/auth/login', { username, password });
        const token = response.data.token; // Ensure the backend sends the JWT token
        console.log('Logged in:', token);
        localStorage.setItem('jwtToken', token); // Store token in localStorage
        return response;
    } catch (error: any) {
        console.log('Error logging in:', error);
        throw error;
    }
};

export const logout = async () => {
    try {
        const response = await apiClient.post('/auth/logout');
        localStorage.removeItem('jwtToken'); // Remove token from localStorage
        return response;
    } catch (error: any) {
        console.log('Error logging out:', error);
        throw error;
    }
};

export const getCurrentUser = async (): Promise<User | null> => {
    let token = localStorage.getItem('jwtToken');
    if (token) {
        token = "Bearer " + token;
        try {
            return <User>jwtDecode(token); // Decode JWT for user information
        } catch (error) {
            console.error('Error decoding token:', error);
            await logout();
        }
    }
    return null;
};
