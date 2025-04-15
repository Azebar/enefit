import { createContext, useContext, useState } from 'react';
import { authApi, AuthResponse, LoginRequest } from '../services/api';

interface AuthContextType {
    isAuthenticated: boolean;
    user: AuthResponse | null;
    username: string | null;
    customerId: number | null;
    login: (data: LoginRequest) => Promise<void>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [user, setUser] = useState<AuthResponse | null>(() => {
        const storedUser = localStorage.getItem('user');
        return storedUser ? JSON.parse(storedUser) : null;
    });

    const login = async (data: LoginRequest) => {
        const response = await authApi.login(data);
        setUser(response.data);
        localStorage.setItem('user', JSON.stringify(response.data));
        localStorage.setItem('token', response.data.token);
    };

    const logout = () => {
        setUser(null);
        localStorage.removeItem('user');
        localStorage.removeItem('token');
    };

    return (
        <AuthContext.Provider
            value={{
                isAuthenticated: !!user,
                user,
                username: user?.username || null,
                customerId: user?.customerId || null,
                login,
                logout,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
}; 