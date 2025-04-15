import React from 'react';
import { useAuth } from '../contexts/AuthContext';
import { consumptionApi, MeteringPoint } from '../services/api';
import ConsumptionChart from './ConsumptionChart';
import { Button } from 'primereact/button';
import { useNavigate } from 'react-router-dom';

export const Dashboard: React.FC = () => {
    const { username, customerId, logout } = useAuth();
    const navigate = useNavigate();
    const [meteringPoints, setMeteringPoints] = React.useState<MeteringPoint[]>([]);
    const [selectedMeteringPoint, setSelectedMeteringPoint] = React.useState<string | null>(null);
    const [loading, setLoading] = React.useState(true);
    const [error, setError] = React.useState<string | null>(null);

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    console.log('Dashboard render:', { customerId, meteringPoints, selectedMeteringPoint });

    // Fetch metering points only when customerId changes
    React.useEffect(() => {
        const fetchMeteringPoints = async () => {
            if (!customerId) {
                console.log('No customerId available, skipping fetch');
                return;
            }
            
            try {
                console.log('Fetching metering points for customerId:', customerId);
                setLoading(true);
                const response = await consumptionApi.getMeteringPoints(customerId);
                console.log('Received metering points:', response.data);
                setMeteringPoints(response.data);
            } catch (err) {
                console.error('Error fetching metering points:', err);
                setError('Failed to load metering points');
            } finally {
                setLoading(false);
            }
        };

        fetchMeteringPoints();
    }, [customerId]);

    const handleMeteringPointChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const value = e.target.value;
        console.log('Metering point selection changed:', value);
        setSelectedMeteringPoint(value === '' ? null : value);
    };

    if (loading) {
        console.log('Rendering loading state');
        return <div>Loading...</div>;
    }
    if (error) {
        console.log('Rendering error state:', error);
        return <div>Error: {error}</div>;
    }
    if (meteringPoints.length === 0) {
        console.log('Rendering empty state - no metering points');
        return <div>No metering points available</div>;
    }

    console.log('Rendering dashboard with selected metering point:', selectedMeteringPoint);
    return (
        <div className="container mx-auto p-4">
            <div className="flex justify-content-between align-items-center mb-4">
                <h1 className="text-2xl font-bold m-0">Welcome, {username}!</h1>
                <Button 
                    label="Logout" 
                    icon="pi pi-sign-out" 
                    severity="secondary" 
                    onClick={handleLogout}
                />
            </div>
            
            <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700">Select Metering Point</label>
                <select
                    className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
                    value={selectedMeteringPoint ?? ''}
                    onChange={handleMeteringPointChange}
                >
                    <option value="">Select a metering point</option>
                    {meteringPoints.map((point) => (
                        <option key={`metering-point-${point.meteringPointId}`} value={point.meteringPointId}>
                            {point.address}
                        </option>
                    ))}
                </select>
            </div>

            {selectedMeteringPoint !== null && (
                <ConsumptionChart meteringPointId={selectedMeteringPoint} />
            )}
        </div>
    );
}; 