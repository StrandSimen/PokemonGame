A work in progress project for PG3402.

This project is a microservice based Pokemon TCG website. Users will be able to:

Open booster packs
* Collect and manage their Pokemon
* Sell Pokemon
* Battle against trainers

How to run:
* cd BoosterPack
* mvn clean install
* cd User
* mvn clean install
* docker compose up

Testing links:

http://localhost:5173/

After opening a BoosterPack, either add it to inventory or sell it and check:

http://localhost:8081/api/defaultUser/inventory

http://localhost:8081/api/defaultUser/coins
