A work in progress project for PG3402.

This project is a microservice based Pokemon TCG website. Users will be able to:

Open booster packs
* Collect and manage their Pokemon
* Sell Pokemon
* Battle against trainers

## Architecture

This project uses a microservices architecture with:
- **Spring Cloud Gateway**: API Gateway with load balancing
- **Consul**: Service discovery and health checking
- **RabbitMQ**: Message broker for async communication
- **PostgreSQL**: Database
- **React Frontend**: User interface

## How to run:

### Build the services:
```cmd
cd boosterpack
mvn clean install
cd ..\user
mvn clean install
cd ..\gateway
mvn clean install
cd ..
```

### Start with Docker Compose:

**Standard deployment (single instance):**
```cmd
docker-compose up --build
```

**Multi-instance deployment (scaled):**
```cmd
docker-compose up --build --scale booster-pack=3 --scale user=2
```

## Testing links:

**Frontend**: http://localhost:5173/

**Consul UI** (Service Discovery): http://localhost:8500

**RabbitMQ Management**: http://localhost:15672 (guest/guest)

After opening a BoosterPack, either add it to inventory or sell it and check:

**User Inventory**: http://localhost:8100/api/defaultUser/inventory

**User Coins**: http://localhost:8100/api/defaultUser/coins

## Monitoring

- **Consul Dashboard**: http://localhost:8500 - View all service instances and health status
- **Service Health**: http://localhost:8100/actuator/health - Gateway health endpoint
