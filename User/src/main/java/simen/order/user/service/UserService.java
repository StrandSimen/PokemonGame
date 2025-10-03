package simen.order.user.service;

import simen.order.user.model.User;
import simen.order.user.repository.UserRepo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    @PostConstruct
    public void initDefaultUser() {
        if (userRepository.count() == 0) {
            User defaultUser = new User();
            defaultUser.setUsername("defaultUser");

            Map<Integer, Integer> inventory = new HashMap<>();
            for (int i = 1; i <= 8; i++) {
                inventory.put(i, 1);
            }
            inventory.put(9, 1);
            inventory.put(1, 2); // Duplicate Pokémon 1 with amount 2

            defaultUser.setInventory(inventory);

            userRepository.save(defaultUser);
            System.out.println("Created default user with 9 unique Pokémon (1-9) and one duplicate of Pokémon 1 with amount 2.");
        }
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

    public Map<Integer, Integer> getPlayerInventory(String username) {
        return userRepository.findById(username)
                .map(User::getInventory)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}