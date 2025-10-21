package simen.order.boosterpack.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import simen.order.boosterpack.services.BoosterpackService;
import simen.order.boosterpack.model.Card;

import java.util.List;

@RestController
@RequestMapping("/api/boosterpack")
public class BoosterpackController {
    private final BoosterpackService boosterpackService;

    public BoosterpackController(BoosterpackService boosterpackService) {
        this.boosterpackService = boosterpackService;
    }

    @GetMapping("/open")
    public Mono<List<Card>> openBooster() {
        return Mono.just(boosterpackService.openBooster());
    }
}