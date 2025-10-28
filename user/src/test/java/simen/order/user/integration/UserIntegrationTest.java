package simen.order.user.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import simen.order.user.config.TestRabbitConfig;
import simen.order.user.model.User;
import simen.order.user.repository.UserRepo;
import simen.order.user.service.UserService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Import(TestRabbitConfig.class)
class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    void setUp() {
        // Clean up test user if exists
        userRepo.deleteById("integrationTestUser");
    }

    @Test
    void testFullUserWorkflow() {
        // 1. Create user
        User user = new User();
        user.setUsername("integrationTestUser");
        user.setCoins(100);
        user.setInventory(new HashMap<>());
        User savedUser = userRepo.save(user);

        assertNotNull(savedUser);
        assertEquals("integrationTestUser", savedUser.getUsername());
        assertEquals(100, savedUser.getCoins());

        // 2. Add cards to inventory
        userService.addToInventory("integrationTestUser", 1);
        userService.addToInventory("integrationTestUser", 1);
        userService.addToInventory("integrationTestUser", 25);

        Map<Integer, Integer> inventory = userService.getPlayerInventory("integrationTestUser");
        assertEquals(2, inventory.get(1));
        assertEquals(1, inventory.get(25));

        // 3. Remove card from inventory
        userService.removeFromInventory("integrationTestUser", 1);
        inventory = userService.getPlayerInventory("integrationTestUser");
        assertEquals(1, inventory.get(1));

        // 4. Sell card
        User updatedUser = userService.sellCard("integrationTestUser", 25, 20);
        assertEquals(120, updatedUser.getCoins());
        assertNull(updatedUser.getInventory().get(25));

        // 5. Get user coins
        int coins = userService.getUserCoins("integrationTestUser");
        assertEquals(120, coins);

        // Cleanup
        userRepo.deleteById("integrationTestUser");
    }

    @Test
    void testUserRepository_SaveAndFind() {
        // Arrange
        User user = new User();
        user.setUsername("repoTestUser");
        user.setCoins(200);
        Map<Integer, Integer> inventory = new HashMap<>();
        inventory.put(1, 3);
        inventory.put(4, 1);
        user.setInventory(inventory);

        // Act
        User saved = userRepo.save(user);
        User found = userRepo.findById("repoTestUser").orElse(null);

        // Assert
        assertNotNull(saved);
        assertNotNull(found);
        assertEquals(200, found.getCoins());
        assertEquals(3, found.getInventory().get(1));
        assertEquals(1, found.getInventory().get(4));

        // Cleanup
        userRepo.deleteById("repoTestUser");
    }

    @Test
    void testProcessBoosterEvent_Integration() {
        // Arrange
        User user = new User();
        user.setUsername("defaultUser");
        user.setCoins(100);
        user.setInventory(new HashMap<>());

        // Clean up and create fresh user
        userRepo.deleteById("defaultUser");
        userRepo.save(user);

        // Act
        String boosterMessage = "1:Bulbasaur, 4:Charmander, 7:Squirtle";
        userService.processBoosterEvent(boosterMessage);

        // Assert
        User updatedUser = userRepo.findById("defaultUser").orElse(null);
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getInventory().get(1));
        assertEquals(1, updatedUser.getInventory().get(4));
        assertEquals(1, updatedUser.getInventory().get(7));
    }

    @Test
    void testUserService_EdgeCases() {
        // Create test user
        User user = new User();
        user.setUsername("edgeCaseUser");
        user.setCoins(50);
        user.setInventory(new HashMap<>());
        userRepo.save(user);

        // Test removing from empty inventory
        assertThrows(RuntimeException.class, () -> {
            userService.removeFromInventory("edgeCaseUser", 1);
        });

        // Add card then remove last one
        userService.addToInventory("edgeCaseUser", 25);
        User afterRemove = userService.removeFromInventory("edgeCaseUser", 25);
        assertNull(afterRemove.getInventory().get(25));

        // Test selling non-existent card
        assertThrows(RuntimeException.class, () -> {
            userService.sellCard("edgeCaseUser", 999, 20);
        });

        // Cleanup
        userRepo.deleteById("edgeCaseUser");
    }
}

