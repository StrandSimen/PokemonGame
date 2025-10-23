package simen.order.loadDB.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import simen.order.loadDB.model.ApiResponse;
import simen.order.loadDB.model.Card;
import simen.order.loadDB.repository.CardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CardService {
    private static final String API_URL = "https://api.pokemontcg.io/v2/cards";
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CardRepo cardRepository;

    @PostConstruct
    public void initDatabase() throws InterruptedException {
        if (cardRepository.count() >= 151) {
            System.out.println("Database already populated with " + cardRepository.count() + " Pokémon.");
            return;
        }

        int attempts = 0;
        while (attempts < 3) {
            try {
                populateDatabase();
                return;
            } catch (RuntimeException e) {
                attempts++;
                System.err.println("Attempt " + attempts + " failed: " + e.getMessage());
                if (attempts < 3) {
                    Thread.sleep(5000);
                } else {
                    System.err.println("Giving up after 3 attempts. Pokémon database may be incomplete.");
                }
            }
        }
    }

    public void populateDatabase() throws InterruptedException {
        if (cardRepository.count() >= 151) {
            System.out.println("Database already populated with " + cardRepository.count() + " Pokémon.");
            return;
        }

        int page = 1;
        boolean morePages = true;
        Set<Integer> existingIds = new HashSet<>(cardRepository.findAll()
                .stream()
                .map(Card::getPokedexNumber)
                .toList());

        List<Card> batch = new ArrayList<>();

        while (morePages) {
            String query = String.format(
                    "?q=supertype:Pok%%C3%%A9mon%%20nationalPokedexNumbers:[1%%20TO%%20151]&pageSize=250&page=%d",
                    page
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + query))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response;
            int attempts = 0;
            boolean success = false;

            while (!success && attempts < 5) {
                try {
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == 200) {
                        ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);
                        List<Card> cards = apiResponse.getData();

                        if (cards == null || cards.isEmpty()) {
                            morePages = false;
                            break;
                        }

                        for (Card card : cards) {
                            if (card.getNationalPokedexNumbers() == null || card.getName() == null) {
                                System.out.println("Skipping invalid card: " + card.getName());
                                continue;
                            }

                            for (Integer dexNum : card.getNationalPokedexNumbers()) {
                                if (dexNum >= 1 && dexNum <= 151 && !existingIds.contains(dexNum)) {
                                    Card clone = new Card();
                                    clone.setPokedexNumber(dexNum);
                                    clone.setName(card.getName());
                                    clone.setHp(card.getHp());
                                    clone.setTypes(card.getTypes());
                                    clone.setImageUrl(card.getImageUrl());

                                    batch.add(clone);
                                    existingIds.add(dexNum);
                                }
                            }
                        }

                        if (!batch.isEmpty()) {
                            cardRepository.saveAll(batch);
                            batch.clear();
                        }

                        success = true;
                    } else if (response.statusCode() == 429) {
                        attempts++;
                        System.out.println("Rate limit hit, retrying in 5s... Attempt " + attempts);
                        Thread.sleep(5000);
                    } else {
                        attempts++;
                        System.out.println("HTTP error " + response.statusCode() + ", retrying... Attempt " + attempts);
                        Thread.sleep(3000);
                    }
                } catch (IOException | InterruptedException e) {
                    attempts++;
                    System.out.println("Exception occurred: " + e.getMessage() + ", retrying... Attempt " + attempts);
                    Thread.sleep(3000);
                }
            }

            if (!success) {
                System.err.println("Failed to fetch page " + page + " after 5 attempts. Continuing...");
            }

            if (existingIds.size() >= 151) {
                morePages = false;
            } else {
                page++;
            }
        }

        System.out.println("Loaded " + existingIds.size() + " unique Pokémon into database.");
    }

    public List<Card> getAllCachedCards() {
        return cardRepository.findAllByOrderByPokedexNumberAsc();
    }

    public Card getPokemon(int pokedexNumber) {
        return cardRepository.findById(pokedexNumber)
                .orElseThrow(() -> new RuntimeException("Pokémon not found with pokedexNumber: " + pokedexNumber));
    }
}