package simen.order.boosterpack.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simen.order.boosterpack.services.BoosterpackService;
import simen.order.card.model.Card;

import java.util.List;

@RestController
@RequestMapping("/api/boosterpack")
public class BoosterpackController {
    private final BoosterpackService boosterpackService;

    public BoosterpackController(BoosterpackService boosterpackService) {
        this.boosterpackService = boosterpackService;
    }

    @GetMapping("/open")
    public List<Card> openBooster() {
        return boosterpackService.openBooster();
    }
}
