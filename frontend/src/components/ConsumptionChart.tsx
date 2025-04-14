import React from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { consumptionApi } from '../services/api';

interface ConsumptionData {
    date: string;
    consumption: number;
    cost: number;
}

interface ConsumptionChartProps {
    meteringPointId: number;
}

export const ConsumptionChart: React.FC<ConsumptionChartProps> = ({ meteringPointId }) => {
    const [data, setData] = React.useState<ConsumptionData[]>([]);
    const [loading, setLoading] = React.useState(true);
    const [error, setError] = React.useState<string | null>(null);

    // Memoize the date range to prevent unnecessary re-renders
    const dateRange = React.useMemo(() => {
        const startDate = new Date();
        startDate.setMonth(startDate.getMonth() - 11); // Last 12 months
        const endDate = new Date();
        return { startDate, endDate };
    }, []);

    React.useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                const response = await consumptionApi.getConsumption(
                    meteringPointId,
                    dateRange.startDate,
                    dateRange.endDate
                );
                setData(response.data);
            } catch (err) {
                setError('Failed to load consumption data');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [meteringPointId, dateRange]);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;
    if (data.length === 0) return <div>No data available</div>;

    return (
        <div style={{ width: '100%', height: 400 }}>
            <h3>Consumption and Cost Over Time</h3>
            <ResponsiveContainer>
                <LineChart data={data}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="date" />
                    <YAxis yAxisId="left" label={{ value: 'Consumption (kWh)', angle: -90, position: 'insideLeft' }} />
                    <YAxis yAxisId="right" orientation="right" label={{ value: 'Cost (€)', angle: 90, position: 'insideRight' }} />
                    <Tooltip />
                    <Legend />
                    <Line
                        yAxisId="left"
                        type="monotone"
                        dataKey="consumption"
                        stroke="#8884d8"
                        name="Consumption (kWh)"
                    />
                    <Line
                        yAxisId="right"
                        type="monotone"
                        dataKey="cost"
                        stroke="#82ca9d"
                        name="Cost (€)"
                    />
                </LineChart>
            </ResponsiveContainer>
        </div>
    );
}; 