Checklist 
To start docker LoadDB for pokemon storage to database : docker run -d -p 5432:5432 --name pokemon-db -e POSTGRES_PASSWORD=password -e POSTGRES_USER=admin -e POSTGRES_DB=pokemondb postgres
