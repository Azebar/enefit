# Enefit Energy Consumption Tracking Application

This application allows customers to track their electricity consumption and costs based on market data from the Baltic region.

## Features

- User authentication (login/logout)
- View electricity consumption data
- View consumption costs based on market prices
- Multiple metering points per customer
- Monthly consumption visualization
- Secure access to personal data
- Real-time cost calculations based on Elering market data
- Logout functionality for secure session management

## Technical Stack

### Backend
- Java 21
- Spring Boot 3
- PostgreSQL
- Gradle
- Liquibase for database migrations
- JWT for authentication
- Elering API integration for market prices

### Frontend
- React
- Vite
- PrimeReact for UI components
- Chart.js for data visualization
- React Router for navigation
- Context API for state management

## Prerequisites

- Docker and Docker Compose
- Java 21 or higher
- Node.js 18 or higher
- npm or yarn

## Environment Variables

### Backend
- `SPRING_DATASOURCE_URL`: PostgreSQL connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `JWT_SECRET`: Secret key for JWT token generation
- `ELERING_API_URL`: Base URL for Elering API

### Frontend
- `VITE_API_URL`: Backend API URL (default: http://localhost:8080)

## Getting Started

1. Clone the repository:
```bash
git clone <repository-url>
cd enefit
```

2. Start the application using Docker Compose:
```bash
docker-compose up -d
```

This will start:
- PostgreSQL database on port 5432
- Backend service on port 8080
- Frontend service on port 3000

3. Access the application:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

## Development

### Backend Development

1. Navigate to the backend directory:
```bash
cd backend
```

2. Run the application:
```bash
./gradlew bootRun
```

### Frontend Development

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm run dev
```

## Testing

### Backend Tests
```bash
cd backend
./gradlew test
```

### Frontend Tests
```bash
cd frontend
npm test
```

The frontend tests use:
- Jest as the test runner
- React Testing Library for component testing
- Mock service worker for API mocking

## Cost Calculation

The application calculates electricity costs based on:
1. Consumption data from metering points
2. Market prices from Elering API
3. Time-based price matching
4. Real-time cost updates

## API Integration

### Elering API
The application integrates with Elering API to fetch:
- Real-time market prices
- Historical price data
- Regional price variations

## Security

- JWT-based authentication
- Secure session management
- Protected API endpoints
- Environment variable configuration
- HTTPS support (in production)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Sample Users

For testing purposes, the following users are available:

1. John Doe
   - Username: john.doe
   - Password: password123
   - One metering point

2. Jane Smith
   - Username: jane.smith
   - Password: password123
   - Two metering points

## API Documentation

The API documentation is available through Swagger UI at http://localhost:8080/swagger-ui.html when the application is running.

## Notes

- The application uses the Elering API (https://estfeed.elering.ee) for market price data
- All API calls to external services are proxied through the backend
- The database is pre-filled with sample data for testing
- JWT tokens are used for authentication with a 24-hour expiration time 