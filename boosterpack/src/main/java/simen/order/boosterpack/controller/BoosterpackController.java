package simen.order.boosterpack.controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simen.order.boosterpack.model.Card;
import simen.order.boosterpack.services.BoosterpackService;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boosterpack")
public class BoosterpackController {
    private final BoosterpackService boosterpackService;

    @Value("${spring.application.name:boosterpack}")
    private String applicationName;

    @Value("${server.port:8082}")
    private String serverPort;

    public BoosterpackController(BoosterpackService boosterpackService) {
        this.boosterpackService = boosterpackService;
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getInstanceInfo() {
        Map<String, String> info = new HashMap<>();
        try {
            info.put("service", applicationName);
            info.put("hostname", InetAddress.getLocalHost().getHostName());
            info.put("ip", InetAddress.getLocalHost().getHostAddress());
            info.put("port", serverPort);
        } catch (UnknownHostException e) {
            info.put("error", "Could not retrieve host information");
        }
        return ResponseEntity.ok(info);
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
