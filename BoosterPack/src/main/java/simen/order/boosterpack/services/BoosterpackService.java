package simen.order.boosterpack.services;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import simen.order.boosterpack.model.Card;
import simen.order.boosterpack.repository.CardRepo;

import java.util.*;

@Service
public class BoosterpackService {
    private final CardRepo cardRepo;
    private final RabbitTemplate rabbitTemplate;
    private final Random random = new Random();

    @Autowired
    public BoosterpackService(CardRepo cardRepo, RabbitTemplate rabbitTemplate) {
        this.cardRepo = cardRepo;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    public Queue boosterQueue() {
        return new Queue("booster.queue", false); // Non-durable queue
    }

    public List<Card> openBooster() {
        List<Card> allCards = cardRepo.findAllByOrderByPokedexNumberAsc();
        List<Card> booster = new ArrayList<>();

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