package simen.order.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simen.order.user.model.User;
import simen.order.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    private final int boosterPackPrice = 20;

    @Autowired
    private UserService userService;

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

    @PostMapping("/{username}/sell")
    public User sellCard(@PathVariable String username, @RequestParam(defaultValue = "20") int sellPrice) {
        return userService.sellCard(username, sellPrice);
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