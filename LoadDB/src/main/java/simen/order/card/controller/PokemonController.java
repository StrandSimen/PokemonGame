package simen.order.card.controller;

import org.springframework.web.bind.annotation.*;
import simen.order.card.model.Card;
import simen.order.card.services.CardService;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {

    private final CardService cardService;

    public PokemonController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/{pokedexNumber}")
    public Card getPokemon(@PathVariable int pokedexNumber) {
        Card card = cardService.getPokemon(pokedexNumber);
        if (card == null) {
            throw new RuntimeException("Pokemon not found");
        }
        return card;
    }

    @GetMapping("/all")
    public Object getAllPokemon() {
        return cardService.getAllCachedCards();
    }
}