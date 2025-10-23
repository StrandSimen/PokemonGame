# Use Node 20 slim image
FROM node:20-slim

# Set working directory
WORKDIR /app

# Copy package files and install dependencies first (cache optimization)
COPY package.json package-lock.json* pnpm-lock.yaml* yarn.lock* ./

# Install dependencies
RUN npm install

# Copy the rest of the app
COPY . .

# Build the app
RUN npm run build

# Expose port 5173 (Vite default) or serve via a static server
EXPOSE 5173

# Start the frontend (using Vite preview or a static server)
CMD ["npx", "vite", "preview", "--port", "5173", "--host"]
