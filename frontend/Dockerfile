FROM node:18-alpine

WORKDIR /app

COPY package*.json ./

# Clean install dependencies
RUN npm ci

# Copy the rest of the application
COPY . .

EXPOSE 3000

# Start the application in development mode
CMD ["npm", "run", "dev", "--", "--host"] 