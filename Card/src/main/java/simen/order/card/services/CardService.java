package simen.order.card.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import simen.order.card.model.ApiResponse;
import simen.order.card.model.Card;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CardService {

    private static final String API_URL = "https://api.pokemontcg.io/v2/cards";
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(java.time.Duration.ofSeconds(30)) // Changed timeout to 30 seconds
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<Integer, Card> cache = new HashMap<>();

    @PostConstruct
    public void preloadCards() throws IOException, InterruptedException {
        int page = 1;
        boolean morePages = true;

        while (morePages) {
            String query = String.format(
                    "?q=supertype:Pok%%C3%%A9mon%%20nationalPokedexNumbers:[1%%20TO%%20151]&pageSize=250&page=%d",
                    page
            );
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + query))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("Failed to fetch Pokémon cards, status: " + response.statusCode());
                break;
            }

            ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);
            List<Card> cards = apiResponse.getData();

            if (cards == null || cards.isEmpty()) {
                morePages = false;
                break;
            }

            for (Card card : cards) {
                if (card.getNationalPokedexNumbers() != null && card.getName() != null) {
                    for (Integer dexNum : card.getNationalPokedexNumbers()) {
                        // Only add first card we see for each species
                        cache.putIfAbsent(dexNum, card);
                    }
                }
            }

            if (cache.size() >= 151) {
                morePages = false;
            } else {
                page++;
            }
        }
        System.out.println("Preloaded " + cache.size() + " unique Pokémon species into memory.");
    }

    public Card getPokemon(int pokedexNumber) {
        return cache.get(pokedexNumber);
    }

    public List<Card> getAllCachedCards() {
        return List.copyOf(cache.values());
    }
}