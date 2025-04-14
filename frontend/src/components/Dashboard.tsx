import React from 'react';
import { useAuth } from '../contexts/AuthContext';
import { consumptionApi, MeteringPoint } from '../services/api';
import { ConsumptionChart } from './ConsumptionChart';

export const Dashboard: React.FC = () => {
    const { username } = useAuth();
    const [meteringPoints, setMeteringPoints] = React.useState<MeteringPoint[]>([]);
    const [selectedMeteringPoint, setSelectedMeteringPoint] = React.useState<number | null>(null);
    const [loading, setLoading] = React.useState(true);
    const [error, setError] = React.useState<string | null>(null);

    React.useEffect(() => {
        const fetchMeteringPoints = async () => {
            try {
                setLoading(true);
                const response = await consumptionApi.getMeteringPoints();
                setMeteringPoints(response.data);
                if (response.data.length > 0 && !selectedMeteringPoint) {
                    setSelectedMeteringPoint(response.data[0].id);
                }
            } catch (err) {
                setError('Failed to load metering points');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchMeteringPoints();
    }, [selectedMeteringPoint]);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-2xl font-bold mb-4">Welcome, {username}!</h1>
            
            <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700">Select Metering Point</label>
                <select
                    className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
                    value={selectedMeteringPoint || ''}
                    onChange={(e) => setSelectedMeteringPoint(Number(e.target.value))}
                >
                    <option value="">Select a metering point</option>
                    {meteringPoints.map((point) => (
                        <option key={`metering-point-${point.id}`} value={point.id}>
                            {point.address}
                        </option>
                    ))}
                </select>
            </div>

            {selectedMeteringPoint && (
                <ConsumptionChart meteringPointId={selectedMeteringPoint} />
            )}
        </div>
    );
}; 