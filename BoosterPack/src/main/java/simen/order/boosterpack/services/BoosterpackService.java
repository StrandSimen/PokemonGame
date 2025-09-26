package simen.order.boosterpack.services;

import org.springframework.stereotype.Service;
import simen.order.card.model.Card;
import simen.order.card.repository.CardRepo;

import java.util.*;

@Service
public class BoosterpackService {
    private final CardRepo cardRepo;
    private final Random random = new Random();

    public BoosterpackService(CardRepo cardRepo) {
        this.cardRepo = cardRepo;
    }

    public List<Card> openBooster() {
        // Hent alle ID-er (1–151)
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
        return booster;
    }
}
