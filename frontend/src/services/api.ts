import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            localStorage.removeItem('token');
            localStorage.removeItem('username');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export interface LoginRequest {
    username: string;
    password: string;
}

export interface AuthResponse {
    token: string;
    username: string;
    role: string;
}

export interface MeteringPoint {
    id: number;
    address: string;
    customerUsername: string;
}

export interface ConsumptionResponse {
    date: string;
    consumption: number;
    cost: number;
}

// Helper function to format date as YYYY-MM-DD
const formatDate = (date: Date): string => {
    return date.toISOString().split('T')[0];
};

export const authApi = {
    login: (data: LoginRequest) => api.post<AuthResponse>('/api/auth/login', data),
};

export const consumptionApi = {
    getMeteringPoints: () => api.get<MeteringPoint[]>('/api/consumption/metering-points'),
    getConsumption: (meteringPointId: number, startDate: Date, endDate: Date) =>
        api.get<ConsumptionResponse[]>(`/api/consumption/metering-points/${meteringPointId}/consumption`, {
            params: { 
                startDate: formatDate(startDate), 
                endDate: formatDate(endDate) 
            },
        }),
    getCosts: (meteringPointId: number, month: Date) =>
        api.get<Record<string, ConsumptionResponse>>(`/api/consumption/metering-points/${meteringPointId}/costs`, {
            params: { 
                month: formatDate(month) 
            },
        }),
}; 