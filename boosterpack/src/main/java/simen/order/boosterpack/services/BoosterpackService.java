package simen.order.boosterpack.services;

import org.springframework.http.ResponseEntity;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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
    private final RabbitTemplate rabbitTemplate;
    private final Random random = new Random();

    @Value("${gateway.api.user.spend-coins}")
    private String spendCoinsUrl;

    @Autowired
    public BoosterpackService(CardRepo cardRepo, RabbitTemplate rabbitTemplate) {
        this.cardRepo = cardRepo;
        this.restTemplate = new RestTemplate();
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    public Queue boosterQueue() {
        return new Queue("booster.queue", false); // Non-durable queue
    }

    public List<Card> openBooster() {
        try {
            restTemplate.postForEntity(spendCoinsUrl, null, String.class);
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

        // Send booster pack details to RabbitMQ
        String message = booster.stream()
                .map(card -> card.getPokedexNumber() + ":" + card.getName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("No cards");
        rabbitTemplate.convertAndSend("booster.queue", message);


        return booster;
    }
}
