FROM node:20-slim
WORKDIR /app
COPY package.json package-lock.json* pnpm-lock.yaml* yarn.lock* ./
RUN npm install
COPY . .
RUN npm run build
EXPOSE 5173
CMD ["npx", "vite", "preview", "--port", "5173", "--host"]
