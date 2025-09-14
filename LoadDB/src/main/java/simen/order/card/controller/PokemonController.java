package simen.order.card.controller;

import simen.order.card.services.CardService;
import simen.order.card.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {

    @Autowired
    private CardService cardService;

    @GetMapping("/all")
    public List<Card> getAllPokemon() {
        return cardService.getAllCachedCards();
    }

    @GetMapping("/{pokedexNumber}")
    public Card getPokemon(@PathVariable int pokedexNumber) {
        return cardService.getPokemon(pokedexNumber);
    }
}