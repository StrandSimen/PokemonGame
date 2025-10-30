# Pokemon Microservices Project

## Project Overview

This is a microservices-based Pokemon Trading Card Game web application developed for PG3402. The project allows users to collect, manage, trade, and battle with Pokemon cards in a scalable distributed system.

### Technology Stack

The project implements a modern microservices architecture with:
- **Spring Boot** microservices (Java 17)
- **Spring Cloud Gateway** for API routing and load balancing
- **Consul** for service discovery and health monitoring
- **RabbitMQ** for asynchronous event-driven communication
- **PostgreSQL** for persistent data storage
- **React** frontend for user interface
- **Docker Compose** for container orchestration
- **Maven** for build management

### Core Features

**Opening Booster Packs:**
- Each booster pack costs 20 coins
- Contains 10 random Pokemon cards
- Cards are automatically added to your inventory via RabbitMQ messaging
- You need enough coins to open a pack, or you'll get an error

**Your Inventory:**
- View all your collected Pokemon cards
- See how many of each card you own
- Cards are stored by their Pokedex number
- Inventory updates automatically when you open packs

**Earning and Spending Coins:**
- Start with coins in your account
- Spend 20 coins to open a booster pack
- Sell unwanted cards to earn coins back (default 20 coins per card)
- Win coins by battling gym trainers (50 coins for winning, 10 coins for losing)

**Battling Gym Trainers:**
- Challenge AI gym leaders (Blaine, Misty, Erika)
- Select 3 Pokemon from your inventory for battle
- Each trainer specializes in a type (Fire, Water, Grass)
- Battle system uses Pokemon HP and type advantages
- Win to earn coins and gym badges


## User Stories
**Player opening boosterpacks:**
1. You will be logged in as "defaultUser" (stored in database)
2. Open booster packs with your coins by pressing "Open Booster Pack" button
3. See the cards you have in your inventory by pressing "View Inventory" button
4. See the coins you have in your account
5. See the coins you have in your account
6. Sell cards you don't want for more coins by pressing sell card button

**Player battling trainers:**
1. Press the "Gym Battle" button from the menu
2. Choose which trainer to battle (Blaine, Misty, Erika)
3. Select 3 Pokemons from your inventory for battle
4. Battle the trainer by pressing "Start Battle" button
5. See the battle result in the battle logs
6. Win to earn coins and gym badges



## How to run:

### Start with Docker Compose:

Docker automatically builds all services using the Maven wrapper, so you do not need to install Maven or run mvn clean install manually.

Run the command below when starting the project for the first time
**Multi-instance deployment (scaled):**
```cmd
docker-compose up --build --scale booster-pack=3 --scale user=2 --scale autoplayers=2
```

**Standard deployment (single instance):**
```cmd
docker-compose up --build
```


### Manuel build (optional)

If you want to build the project manually, you can run the commands below:
```cmd
cd boosterpack
mvnw clean install
cd ..\user
mvnw clean install
cd ..\gateway
mvnw clean install
cd ..\autoplayers
mvnw clean install
cd ..
```


## Architecture

This project uses a microservices architecture with:
- **Spring Cloud Gateway**: API Gateway with load balancing
- **Consul**: Service discovery and health checking
- **RabbitMQ**: Message broker for async communication
- **PostgreSQL**: Database
- **React Frontend**: User interface

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              Docker Compose Environment                          │
└─────────────────────────────────────────────────────────────────────────────────┘

                              ┌──────────────────┐
                              │   Frontend       │
                              │   (React)        │
                              │   Port: 5173     │
                              └────────┬─────────┘
                                       │
                                       │ HTTP/REST (Synchronous)
                                       │
                                       ▼
                    ┌─────────────────────────────────────┐
                    │   Spring Cloud Gateway              │
                    │   Port: 8100                        │
                    │   (API Gateway + Load Balancing)    │
                    └──────┬────────────────────┬─────────┘
                           │                    │
          HTTP/REST        │                    │ Service Discovery
          (Synchronous)    │                    │ (Synchronous)
                           │                    │
                           ▼                    ▼
        ┌──────────────────────────┐   ┌─────────────────────┐
        │                          │   │     Consul          │
        │   Microservices Layer    │   │     Port: 8500      │
        │                          │   │  (Service Registry  │
        └──────────────────────────┘   │   & Health Check)   │
                                       └──────────┬──────────┘
                                                  │
                                                  │ Registration & 
                                                  │ Health Checks
                                                  │ (Synchronous)
        ┌─────────────────────────────────────────┼─────────────────────────┐
        │                                         │                         │
        │                                         │                         │
        ▼                                         ▼                         ▼
┌───────────────┐                      ┌──────────────────┐     ┌──────────────────┐
│ BoosterPack   │                      │  User Service    │     │  Autoplayers     │
│ Service(s)    │                      │  Port: 8081      │     │  Service(s)      │
│ Port: 8082    │                      │  (Scalable: x2)  │     │  Port: 8083      │
│(Scalable: x3) │                      │                  │     │  (Scalable: x2)  │
└───────┬───────┘                      └────────┬─────────┘     └────────┬─────────┘
        │                                       │                        │
        │                                       │                        │
        │  Async Message                        │ Consume                │
        │  Publishing                           │ Messages               │ Consume
        │  (Asynchronous)                       │ (Asynchronous)         │ Messages
        │                                       │                        │ (Asynchronous)
        │         ┌─────────────────────────────┴────────────────────────┘
        │         │
        └────────►│
                  │
           ┌──────▼──────────┐
           │   RabbitMQ      │
           │   Port: 5672    │
           │   (Web: 15672)  │
           │   Message Broker│
           └─────────────────┘


        ┌──────────────┬─────────────────┬──────────────────┐
        │              │                 │                  │
        │ JDBC         │ JDBC            │ JDBC             │
        │ (Sync)       │ (Sync)          │ (Sync)           │
        │              │                 │                  │
        ▼              ▼                 ▼                  ▼
     ┌──────────────────────────────────────────────────────────┐
     │              PostgreSQL Database                          │
     │              Port: 5432                                   │
     │         (Shared by all microservices)                     │
     └──────────────────────────────────────────────────────────┘


═══════════════════════════════════════════════════════════════════════════
Communication Types:
═══════════════════════════════════════════════════════════════════════════

SYNCHRONOUS (Request-Response):
  • Frontend ↔ Gateway                  : HTTP/REST
  • Gateway ↔ BoosterPack Service       : HTTP/REST (Load Balanced)
  • Gateway ↔ User Service              : HTTP/REST (Load Balanced)
  • Gateway ↔ Autoplayers Service       : HTTP/REST (Load Balanced)
  • All Services ↔ Consul               : Service Registration & Discovery
  • All Services ↔ PostgreSQL           : JDBC Database Connections

ASYNCHRONOUS (Event-Driven):
  • BoosterPack → RabbitMQ → User       : Booster pack opened events
  • BoosterPack → RabbitMQ → Autoplayers: Game events/notifications
  • User → RabbitMQ → Autoplayers       : User action events

═══════════════════════════════════════════════════════════════════════════
Service Responsibilities:
═══════════════════════════════════════════════════════════════════════════

• Frontend:          User interface for opening booster packs and viewing inventory
• Gateway:           Single entry point, load balancing, routing to microservices
• Consul:            Service discovery, health monitoring, dynamic service registry
• BoosterPack:       Opens booster packs, deducts coins, publishes events to RabbitMQ
• User:              Manages user inventory, coins, and card collection
• Autoplayers:       Automated game players/bots (consumes game events)
• RabbitMQ:          Message broker for asynchronous event-driven communication
• PostgreSQL:        Persistent data storage for all microservices

═══════════════════════════════════════════════════════════════════════════
```


## Testing links:

**Frontend**: http://localhost:5173/

**Consul UI** (Service Discovery): http://localhost:8500 

**RabbitMQ Management**: http://localhost:15672 (guest/guest)

After opening a BoosterPack, it will be available at:

**User Inventory**: http://localhost:8100/api/defaultUser/inventory

After adding a Coin to the inventory, it will be available at:

**User Coins**: http://localhost:8100/api/defaultUser/coins

To view all cards in the database, go to:

**Cards**: http://localhost:8100/api/cards 

## Monitoring
- **Service Health**: http://localhost:8100/actuator/health - Gateway health endpoint

## Contributions and responsibilities 
In this project, Simen and Halvor worked together. We started by agreeing on what to build and how it should look. As we began coding, tasks were divided so we could work in our own branches in GitHub and push to main when ready. We also used a Google document to keep track of who was doing what at any time. Both worked on setting up the structure, and eventually Simen took more responsibility for the game logic and frontend. Halvor then focused on setting up the Gateway, Consul, Rabbit, and tests.


## Testing

Project includes comprehensive tests with H2 in-memory database and Mockito.
## Run all tests:

**For all services:**
```cmd
go to each module in maven, lifecycle and press 'test'
```

**For testing with maven wrapper in the terminal:**
```cmd
cd boosterpack
mvnw test

cd ..\user
mvnw test
```

### Test-types:

#### 1. **Unit Tests** (Service and Controller layers)
Uses Mockito to mock dependencies:
- `BoosterpackServiceTest.java` - Tests booster pack logic
- `UserServiceTest.java` - Tests user service logic
- `BoosterpackControllerTest.java` - Tests REST endpoints (WebFlux)
- `UserControllerTest.java` - Tests REST endpoints (WebMvc)

#### 2. **Integration Tests**
Tests with real H2 database:
- `BoosterPackIntegrationTest.java` - Tests repository and database operations
- `UserIntegrationTest.java` - Tests full workflow from service to database

### Test Coverage:

**BoosterPack Service:**
- Open booster pack successfully
- Error handling for insufficient coins
- RabbitMQ message formatting
- Handling cards from available pool
- Repository operations (CRUD, ordering)

**User Service:**
- Add card to inventory (new and existing cards)
- Remove card from inventory
- Sell card with coin updates
- Get user inventory and coins
- Process RabbitMQ booster events
- Error handling (user not found, empty inventory, invalid card)
- Full user workflow (create → add → remove → sell)

**Controllers:**
- GET and POST endpoints
- Success and error responses
- Instance info endpoints for load balancing
- HTTP status codes (200, 400)

### Test Configuration:

Tests use:
- **H2 Database**: In-memory database (jdbc:h2:mem:testdb)
- **@ActiveProfiles("test")**: Activates test profile
- **Mockito**: Mocks external dependencies (RestTemplate, RabbitMQ)
- **Disabled Services**: Consul and RabbitMQ are disabled in test mode

### Run tests during build:

```cmd
# Build with tests (default)
mvn clean install

# Build without tests (faster)
mvn clean install -DskipTests
```

### Test Reports:

After running tests, find reports here:
```
boosterpack/target/surefire-reports/
user/target/surefire-reports/
```

