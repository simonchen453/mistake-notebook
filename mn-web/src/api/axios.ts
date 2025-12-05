import axios from 'axios';
import { getToken } from '../utils/auth';

const api = axios.create({
    baseURL: '/api',
    timeout: 10000,
});

api.interceptors.request.use(
    (config) => {
        const token = getToken();
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`; // Or whatever backend expects
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (error.response && error.response.status === 401) {
            // Handle unauthorized (redirect to login handled in app or here)
            // window.location.href = '/login'; // Optional: force redirect
        }
        return Promise.reject(error);
    }
);

export default api;
