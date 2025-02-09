import axios, {AxiosInstance} from 'axios';

// Create an Axios instance
const apiClient: AxiosInstance = axios.create({
    baseURL: 'http://localhost:8080/StudentsVoice-v1.0/api',
    headers: {
        'Content-Type': 'application/json',
    },
});

// Add a request interceptor to include the JWT token
apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('jwtToken');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Add a response interceptor to handle session expiration
apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            console.error('Unauthorized. Redirecting to login...');
            localStorage.removeItem('jwtToken'); // Clear the token
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default apiClient;
