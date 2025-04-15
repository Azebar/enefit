import React, { useEffect, useState } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { consumptionApi, ConsumptionData } from '../services/api';

interface ConsumptionChartProps {
    meteringPointId: string;
}

const ConsumptionChart: React.FC<ConsumptionChartProps> = ({ meteringPointId }) => {
    const [data, setData] = useState<ConsumptionData[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                setError(null);
                console.log('Fetching consumption data for metering point:', meteringPointId);
                const response = await consumptionApi.getConsumptionByMeteringPoint(meteringPointId);
                console.log('Raw consumption data:', response.data);
                setData(response.data);
            } catch (err) {
                console.error('Error fetching consumption data:', err);
                setError('Failed to load consumption data');
            } finally {
                setLoading(false);
            }
        };

        if (meteringPointId) {
            fetchData();
        }
    }, [meteringPointId]);

    if (loading) {
        return <div>Loading consumption data...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    if (!data.length) {
        return <div>No consumption data available</div>;
    }

    // Format data for the chart
    const chartData = data.map(item => {
        console.log('Processing item:', item);
        console.log('Raw timestamp:', item.timestamp);
        
        // Parse the ISO date string and format it
        const [datePart] = item.timestamp.split('T');
        const [year, month, day] = datePart.split('-');
        const date = new Date(Number(year), Number(month) - 1, Number(day));
        
        return {
            ...item,
            consumptionTime: date.toLocaleDateString('en-US', { 
                year: 'numeric',
                month: 'short'
            }),
            amount: item.consumption ? Number(item.consumption.toFixed(2)) : 0,
            cost: item.cost ? Number(item.cost.toFixed(2)) : 0
        };
    });

    return (
        <div style={{ width: '100%', height: 400 }} data-testid="consumption-chart">
            <ResponsiveContainer>
                <LineChart data={chartData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="consumptionTime" />
                    <YAxis yAxisId="left" />
                    <YAxis yAxisId="right" orientation="right" />
                    <Tooltip />
                    <Legend />
                    <Line
                        yAxisId="left"
                        type="monotone"
                        dataKey="amount"
                        stroke="#8884d8"
                        name="Consumption (kWh)"
                    />
                    <Line
                        yAxisId="right"
                        type="monotone"
                        dataKey="cost"
                        stroke="#82ca9d"
                        name="Cost (EUR)"
                    />
                </LineChart>
            </ResponsiveContainer>
        </div>
    );
};

export default ConsumptionChart; 