package simen.order.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simen.order.user.model.User;
import simen.order.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    private final int boosterPackPrice = 20;

    @Autowired
    private UserService userService;

    @Value("${spring.application.name:user}")
    private String applicationName;

    @Value("${server.port:8081}")
    private String serverPort;

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

    @GetMapping("/{username}/inventory")
    public Map<Integer, Integer> getPlayerInventory(@PathVariable String username) {
        return userService.getPlayerInventory(username);
    }

    @GetMapping("/{username}/coins")
    public int getUserCoins(@PathVariable String username) {
        return userService.getUserCoins(username);
    }

    @PostMapping("/{username}/add/{pokedexNumber}")
    public User addToInventory(@PathVariable String username, @PathVariable int pokedexNumber) {
        return userService.addToInventory(username, pokedexNumber);
    }

    @PostMapping("/{username}/remove/{pokedexNumber}")
    public  User removeFromInventory(@PathVariable String username, @PathVariable int pokedexNumber) {
        return userService.removeFromInventory(username, pokedexNumber);
    }

    @PostMapping("/{username}/sell/{pokedexNumber}")
    public User sellCard(@PathVariable String username, @PathVariable int pokedexNumber, @RequestParam(defaultValue = "20") int sellPrice) {
        return userService.sellCard(username, pokedexNumber, sellPrice);
    }

    //Hardcoded to defaultUser for now
    @PostMapping("defaultUser/spendCoins")
    public ResponseEntity<String> spendCoins() {
        User user = userService.getUser("defaultUser");
        if (user.getCoins() < boosterPackPrice) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough coins");
        }
        user.setCoins(user.getCoins() - boosterPackPrice);
        userService.saveUser(user);
        return ResponseEntity.ok("Coins deducted");
    }
}