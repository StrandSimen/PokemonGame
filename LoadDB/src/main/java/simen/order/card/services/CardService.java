package simen.order.card.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import simen.order.card.model.ApiResponse;
import simen.order.card.model.Card;
import simen.order.card.repository.CardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Service
public class CardService {

    private static final String API_URL = "https://api.pokemontcg.io/v2/cards";
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CardRepo cardRepository;  // Inject repository

    @PostConstruct
    public void initDatabase() throws IOException, InterruptedException {
        if (cardRepository.count() == 0) {  // Load only if DB is empty
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

                // Save unique cards to DB
                for (Card card : cards) {
                    if (card.getPokedexNumber() != null && !cardRepository.existsById(card.getPokedexNumber())) {
                        cardRepository.save(card);
                    }
                }

                if (cardRepository.count() >= 151) {
                    morePages = false;
                } else {
                    page++;
                }
            }
            System.out.println("Loaded " + cardRepository.count() + " unique Pokémon into database.");
        } else {
            System.out.println("Database already populated with " + cardRepository.count() + " Pokémon.");
        }
    }

    public List<Card> getAllCachedCards() {
        return cardRepository.findAllByOrderByPokedexNumberAsc();
    }

    public Card getPokemon(int pokedexNumber) {
        return cardRepository.findById(pokedexNumber)
                .orElseThrow(() -> new RuntimeException("Pokemon not found with pokedexNumber: " + pokedexNumber));
    }
}