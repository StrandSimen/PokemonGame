# Pokemon Microservices Project
GitHub: https://github.com/StrandSimen/PokemonGame

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
- Contains 11 random Pokemon cards
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
5. Sell cards you don't want for more coins by pressing sell card button

**Player battling trainers:**
1. Press the "Gym Battle" button from the menu
2. Choose which trainer to battle (Blaine, Misty, Erika)
3. Select 3 Pokemons from your inventory for battle
4. Battle the trainer by pressing "Start Battle" button
5. See the battle result in the battle logs
6. Win to earn coins and gym badges



## How to run:

### Start with Docker Compose:

Docker automatically builds all services using the Maven wrapper, so you do **not** need to install Maven or run mvn clean install manually.

Run the command below when starting the project for the first time and when you want to start it again.

**Multi-instance deployment (scaled):**
```cmd
docker-compose up --build --scale booster-pack=3 --scale user=2 --scale autoplayers=2
```
**To stop the project:**
```cmd
docker-compose down  
```
**To stop and start without rebuild**
```cmd
docker-compose stop
docker-compose start
```
**Standard deployment (single instance):**
```cmd
docker-compose up --build
```
**Tips**
The project uses a few minutes to start up completly and show all the service in consul. 
If something goes wrong, like one service doesn't start, you can try to start it manually in docker desktop. 

## Testing links:

**Frontend**: http://localhost:5173/ This is where you start the game.

**Consul UI** (Service Discovery): http://localhost:8500 This is where you can see all services and their health status.

**RabbitMQ Management**: http://localhost:15672 (guest/guest) This is where you can see all messages and queues.

After opening a BoosterPack, it will be available at:

**User Inventory**: http://localhost:8100/api/defaultUser/inventory This is where you can see all cards in your inventory.

After adding a Coin to the inventory, it will be available at:

**User Coins**: http://localhost:8100/api/defaultUser/coins This is where you can see all coins in your account.

To view all cards in the database, go to:

**Cards**: http://localhost:8100/api/cards This is where you can see all cards in the database.

## Monitoring
- **Service Health**: http://localhost:8100/actuator/health - Gateway health endpoint

## Architecture

This project uses a microservices architecture with:
- **Spring Cloud Gateway**: API Gateway with load balancing
- **Consul**: Service discovery and health checking
- **RabbitMQ**: Message broker for async communication
- **PostgreSQL**: Database
- **React Frontend**: User interface

## Architecture Diagram

<img width="1361" height="2045" alt="Diagram" src="https://github.com/user-attachments/assets/eaca0428-e035-4e02-8cec-a44daac6bdbf" />





**Communication Flow:**

**SYNCHRONOUS (HTTP/REST - Request/Response):**
  • Frontend → Gateway → Microservices (all user actions)
  
  • BoosterPack → Gateway → User Service (spendCoins endpoint)
     • When opening a booster pack, BoosterPack calls User service to deduct 20 coins
     • Uses RestTemplate with gateway URL: http://gateway:8100/api/defaultUser/spendCoins
     • Waits for response to confirm coins were deducted before generating cards
     
  • Autoplayers → User Service (inventory endpoint via Load Balancer)
     • When starting a gym battle, Autoplayers calls User service to validate Pokemon ownership
     • Uses WebClient with service URL: http://user/api/{username}/inventory
     • Waits for response to confirm player owns all 3 Pokemon before starting battle
     • Load balanced across multiple User service instances via Consul discovery
     
  • All services register with Consul for service discovery
  
  • Gateway uses Consul to find and load balance between service instances

**ASYNCHRONOUS (RabbitMQ - Event-Driven):**
  • booster.queue:  BoosterPack publishes → User consumes (add cards to inventory)
  • gym.queue:      Autoplayers publishes → User consumes (battle rewards/coins)

**DATABASE (PostgreSQL):**
  • Shared database accessed by all microservices for persistent storage
  • Stores: Users, Cards, Inventory, Coins, Gym Trainers, Battle History

**Service Responsibilities**:

• Frontend:          React UI for opening packs, viewing inventory, and battling trainers

• Gateway:           API Gateway - single entry point with load balancing and routing

• Consul:            Service discovery, health monitoring, dynamic service registry

• BoosterPack:       Opens booster packs, deducts coins, publishes card lists to RabbitMQ

• User:              Manages inventory, coins, consumes booster and gym reward events

• Autoplayers:       Gym battle system, AI trainers, publishes battle rewards to RabbitMQ

• RabbitMQ:          Message broker for asynchronous communication between services

• PostgreSQL:        Persistent data storage shared by all microservices




## Architecture Decisions and Simplifications

**Shared Database:**
For simplicity, all microservices currently share a single PostgreSQL instance. In a real-world production setup, each service would typically have its own database to ensure loose coupling and better scalability. This separation could be implemented in a future iteration of the project.

**Simplified Battle AI:**
Gym trainers are AI-controlled and use fixed, predictable logic. Developing a complex battle engine with advanced AI was outside the project’s main scope but remains an interesting area for future expansion.

**Default User and Placeholders:**
The application currently uses a default user ("defaultUser") instead of a full authentication system. Implementing login and registration was not prioritized in this version. However, most endpoints are prepared for future user support by using dynamic placeholders like {username} instead of hardcoded values.

**Predefined Pokémon Data:**
The database is prefilled with Pokémon card data using a .sql file generated by the loaddb module(db-init folder). The loader module retrieves the 151 original Pokémon from the public Pokémon TCG API (https://docs.pokemontcg.io/
). Since the API import process takes several minutes to complete, we decided to pre-generate the dataset for efficiency. The loaddb code is still included in the repository to demonstrate how the import was implemented.




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

