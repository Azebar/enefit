# Enefit Energy Consumption Tracking Application

This application allows customers to track their electricity consumption and costs based on market data from the Baltic region.

## Features

- User authentication (login/logout)
- View electricity consumption data
- View consumption costs based on market prices
- Multiple metering points per customer
- Monthly consumption visualization
- Secure access to personal data

## Technical Stack

### Backend
- Java 21
- Spring Boot 3
- PostgreSQL
- Gradle
- Liquibase for database migrations
- JWT for authentication

### Frontend
- React
- Vite
- PrimeReact for UI components
- Chart.js for data visualization

## Prerequisites

- Docker and Docker Compose
- Java 21 or higher
- Node.js 18 or higher
- npm or yarn

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