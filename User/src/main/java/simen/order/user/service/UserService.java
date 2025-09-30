package simen.order.user.service;

import simen.order.user.model.User;
import simen.order.user.repository.UserRepo;
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

            // Initialize inventory with specific Pokémon indices (1-9) and one duplicate
            Map<Integer, Integer> inventory = new HashMap<>();
            // Add Pokémon 1 to 8 with amount 1
            for (int i = 1; i <= 8; i++) {
                inventory.put(i, 1);
            }
            // Add Pokémon 9 with amount 1
            inventory.put(9, 1);
            // Add duplicate Pokémon 1 with amount 2 (overwrites the previous entry for ID 1)
            inventory.put(1, 2);

            defaultUser.setInventory(inventory);

            userRepository.save(defaultUser);
            System.out.println("Created default user with 9 unique Pokémon (1-9) and one duplicate of Pokémon 1 with amount 2.");
        }
    }

    public Map<Integer, Integer> getPlayerInventory(String username) {
        return userRepository.findById(username)
                .map(User::getInventory)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}