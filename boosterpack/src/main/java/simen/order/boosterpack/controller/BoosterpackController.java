package simen.order.boosterpack.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simen.order.boosterpack.model.Card;
import simen.order.boosterpack.services.BoosterpackService;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boosterpack")
public class BoosterpackController {
    private final BoosterpackService boosterpackService;

    public BoosterpackController(BoosterpackService boosterpackService) {
        this.boosterpackService = boosterpackService;
    }

    @GetMapping("/open")
    public ResponseEntity<?> openPack() {
        try {
            List<Card> cards = boosterpackService.openBooster();
            return ResponseEntity.ok(cards);
        } catch (RuntimeException e) {
            // Send the exception message in the response body
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
