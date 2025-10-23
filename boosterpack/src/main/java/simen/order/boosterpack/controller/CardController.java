package simen.order.boosterpack.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simen.order.boosterpack.model.Card;
import simen.order.boosterpack.repository.CardRepo;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    private final CardRepo cardRepo;

    public CardController(CardRepo cardRepo) {
        this.cardRepo = cardRepo;
    }

    @GetMapping
    public List<Card> getAllCards() {
        return cardRepo.findAllByOrderByPokedexNumberAsc();
    }

    @GetMapping("/{pokedexNumber}")
    public Card getCard(@PathVariable Integer pokedexNumber) {
        return cardRepo.findById(pokedexNumber)
                .orElseThrow(() -> new RuntimeException("Card not found: " + pokedexNumber));
    }
}
