import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import { Password } from 'primereact/password';
import { Button } from 'primereact/button';
import { Message } from 'primereact/message';
import { useAuth } from '../contexts/AuthContext';
import logo from '../assets/logo.svg';

export function Login() {
    const navigate = useNavigate();
    const { login } = useAuth();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            await login({ username, password });
            navigate('/dashboard');
        } catch (err) {
            setError('Failed to login');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex justify-content-center align-items-center" style={{ height: '100vh' }}>
            <div className="flex flex-column align-items-center gap-4">
                <img src={logo} alt="Enefit Logo" style={{ width: '150px', marginBottom: '1rem' }} />
                <Card title="Login" className="w-30rem">
                    <form onSubmit={handleSubmit} className="flex flex-column gap-3">
                        {error && <Message severity="error" text={error} />}
                        <div className="flex flex-column gap-2">
                            <label htmlFor="username">Username</label>
                            <InputText
                                id="username"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                            />
                        </div>
                        <div className="flex flex-column gap-2">
                            <label htmlFor="password">Password</label>
                            <Password
                                id="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                feedback={false}
                                required
                            />
                        </div>
                        <Button type="submit" label="Login" loading={loading} />
                    </form>
                </Card>
            </div>
        </div>
    );
} 