package simen.order.boosterpack.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import simen.order.boosterpack.model.Card;
import simen.order.boosterpack.repository.CardRepo;

import java.util.*;

@Service
public class BoosterpackService {
    private final CardRepo cardRepo;
    private final RestTemplate restTemplate;
    private final Random random = new Random();

    public BoosterpackService(CardRepo cardRepo) {
        this.cardRepo = cardRepo;
        this.restTemplate = new RestTemplate();
    }

    public List<Card> openBooster() {
        String userServiceAddUrlTemplate = "http://user:8081/api/defaultUser/add/%d";
        String userServiceUrl = "http://user:8081/api/defaultUser/spendCoins";

        try {
            restTemplate.postForEntity(userServiceUrl, null, String.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Not enough coins to open booster pack");
        }

        List<Card> allCards = cardRepo.findAllByOrderByPokedexNumberAsc();
        List<Card> booster = new ArrayList<>();

        // Trekker 10 unike random kort
        Set<Integer> chosenIndexes = new HashSet<>();
        while (chosenIndexes.size() < 10) {
            int index = random.nextInt(allCards.size());
            if (chosenIndexes.add(index)) {
                booster.add(allCards.get(index));
            }
        }

        for (Card card : booster) {
            String userServiceAddUrl = String.format(userServiceAddUrlTemplate, card.getPokedexNumber());
            try {
                restTemplate.postForEntity(userServiceAddUrl, null, String.class);
            } catch (HttpClientErrorException e) {
                // Optional: log failed additions but continue
                System.err.println("Failed to add card " + card.getPokedexNumber() + " to inventory");
            }
        }

        return booster;
    }
}
