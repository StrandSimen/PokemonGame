package simen.order.user.service;

import simen.order.user.model.User;
import simen.order.user.repository.UserRepo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    @PostConstruct
    public void initDefaultUser() {
        User defaultUser = new User();
        defaultUser.setUsername("defaultUser");
        userRepository.save(defaultUser);
    }

    public User addToInventory(String username, int pokedexNumber) {
        User user = getUser(username);

        user.getInventory().merge(pokedexNumber, 1, Integer::sum);
        return userRepository.save(user);
    }

    public User removeFromInventory(String username, int pokedexNumber) {
        User user = getUser(username);
        Map<Integer, Integer> inventory = user.getInventory();

        if (inventory == null || inventory.isEmpty()) {
            throw new RuntimeException("Inventory is empty");
        }

        Integer keyToRemove = inventory.containsKey(pokedexNumber)
                ? pokedexNumber
                : inventory.keySet().stream()
                .filter(k -> String.valueOf(k).equals(String.valueOf(pokedexNumber)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot remove: Pokémon not in inventory"));

        inventory.computeIfPresent(keyToRemove, (key, value) -> value > 1 ? value - 1 : null);

        return userRepository.save(user);
    }

    public Map<Integer, Integer> getPlayerInventory(String username) {
        return userRepository.findById(username)
                .map(User::getInventory)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public User sellCard(String username, int pokedexNumber, int sellPrice) {
        User user = removeFromInventory(username, pokedexNumber);

        user.setCoins(user.getCoins() + sellPrice);


        return userRepository.save(user);
    }

    public int getUserCoins(String username) {
        User user = getUser(username);
        return user.getCoins();
    }

    public User getUser(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new RuntimeException("User not found: " + username));
        return user;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @RabbitListener(queues = "booster.queue")
    public void processBoosterEvent(String message) {
        User defaultUser = userRepository.findById("defaultUser")
                .orElseThrow(() -> new RuntimeException("Default user not found"));
        Map<Integer, Integer> inventory = defaultUser.getInventory();

        // Parse the message (e.g., "1:Bulbasaur, 2:Ivysaur")
        String[] cardStrings = message.split(", ");
        for (String cardString : cardStrings) {
            try {
                String[] parts = cardString.split(":");
                if (parts.length == 2) {
                    int pokedexNumber = Integer.parseInt(parts[0].trim());
                    inventory.merge(pokedexNumber, 1, Integer::sum); // Add 1 or increment
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid card format in message: " + cardString);
            }
        }

        userRepository.save(defaultUser);
        System.out.println("Updated inventory with new booster pack: " + inventory);
    }

    @RabbitListener(queues = "gym.queue")
    public void processGymReward(String message) {
        try {
            // Parse message format: "BATTLE_REWARD:username:coins"
            if (message.startsWith("BATTLE_REWARD:")) {
                String[] parts = message.split(":");
                if (parts.length == 3) {
                    String username = parts[1];
                    int coins = Integer.parseInt(parts[2]);

                    User user = userRepository.findById(username)
                            .orElseThrow(() -> new RuntimeException("User not found: " + username));

                    user.setCoins(user.getCoins() + coins);
                    userRepository.save(user);

                    System.out.println("Awarded " + coins + " coins to " + username + " for gym battle victory!");
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing gym reward: " + e.getMessage());
        }
    }
}