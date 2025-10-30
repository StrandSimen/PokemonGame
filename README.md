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

### Start with Docker Compose:

Docker bygger automatisk alle services med Maven wrapper når du kjører `--build` flagget.

**Standard deployment (single instance):**
```cmd
docker-compose up --build
```

**Multi-instance deployment (scaled):**
```cmd
docker-compose up --build --scale booster-pack=3 --scale user=2 --scale autoplayers=2
```

### Manuell bygging (valgfritt):

Hvis du ønsker å bygge services manuelt før Docker-bygging:
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

## Testing

Prosjektet inkluderer omfattende tester med H2 in-memory database og Mockito.

### Kjør alle tester:

**For alle services:**
```cmd
gå i hver modul i maven, lifecycle og trykk 'test'
```

**For spesifikk service:**
```cmd
cd boosterpack
mvn test

cd ..\user
mvn test
```

### Test-typer:

#### 1. **Unit Tests** (Service og Controller lag)
Bruker Mockito for å mocke avhengigheter:
- `BoosterpackServiceTest.java` - Tester booster pack logikk
- `UserServiceTest.java` - Tester user service logikk
- `BoosterpackControllerTest.java` - Tester REST endpoints (WebFlux)
- `UserControllerTest.java` - Tester REST endpoints (WebMvc)

#### 2. **Integration Tests**
Tester med ekte H2 database:
- `BoosterPackIntegrationTest.java` - Tester repository og database operasjoner
- `UserIntegrationTest.java` - Tester full workflow fra service til database

### Test Coverage:

**BoosterPack Service:**
-  Åpne booster pack med nok coins
-  Feilhåndtering ved ikke nok coins
-  Feilhåndtering ved problemer med å legge til kort
-  RabbitMQ message formatting
-  Håndtering av få tilgjengelige kort
-  Repository operasjoner (CRUD, sortering)

**User Service:**
-  Legge til kort i inventory (nye og eksisterende)
-  Fjerne kort fra inventory
-  Selge kort med coin-oppdatering
-  Få bruker inventory og coins
-  Prosessere RabbitMQ booster events
-  Feilhåndtering (bruker ikke funnet, tom inventory, ugyldig kort)
-  Full bruker workflow (create → add → remove → sell)

**Controllers:**
-  GET og POST endpoints
-  Success og error responses
-  Instance info endpoints for load balancing
-  HTTP status koder (200, 400)

### Test-konfigurasjon:

Testene bruker:
- **H2 Database**: In-memory database (jdbc:h2:mem:testdb)
- **@ActiveProfiles("test")**: Aktiverer test-profil
- **Mockito**: Mocker eksterne avhengigheter (RestTemplate, RabbitMQ)
- **Disabled Services**: Consul og RabbitMQ er deaktivert i test-modus

### Kjør tester under build:

```cmd
# Build med tester (standard)
mvn clean install

# Build uten tester (raskere)
mvn clean install -DskipTests
```

### Test Reports:

Etter å ha kjørt tester, finn rapporter her:
```
boosterpack/target/surefire-reports/
user/target/surefire-reports/
```

