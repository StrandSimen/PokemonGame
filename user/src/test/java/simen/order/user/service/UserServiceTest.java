package simen.order.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import simen.order.user.model.User;
import simen.order.user.repository.UserRepo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setCoins(100);
        testUser.setInventory(new HashMap<>());
    }

    @Test
    void testAddToInventory_NewCard() {
        // Arrange
        when(userRepository.findById("testUser")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.addToInventory("testUser", 25);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getInventory().get(25));
        verify(userRepository).save(testUser);
    }

    @Test
    void testAddToInventory_ExistingCard() {
        // Arrange
        testUser.getInventory().put(25, 2);
        when(userRepository.findById("testUser")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.addToInventory("testUser", 25);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getInventory().get(25));
        verify(userRepository).save(testUser);
    }

    @Test
    void testAddToInventory_UserNotFound() {
        // Arrange
        when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userService.addToInventory("nonexistent", 25);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRemoveFromInventory_Success() {
        // Arrange
        testUser.getInventory().put(25, 3);
        when(userRepository.findById("testUser")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.removeFromInventory("testUser", 25);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getInventory().get(25));
        verify(userRepository).save(testUser);
    }

    @Test
    void testRemoveFromInventory_LastCard() {
        // Arrange
        testUser.getInventory().put(25, 1);
        when(userRepository.findById("testUser")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.removeFromInventory("testUser", 25);

        // Assert
        assertNotNull(result);
        assertNull(result.getInventory().get(25), "Card should be removed when count reaches 0");
        verify(userRepository).save(testUser);
    }

    @Test
    void testRemoveFromInventory_EmptyInventory() {
        // Arrange
        when(userRepository.findById("testUser")).thenReturn(Optional.of(testUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.removeFromInventory("testUser", 25);
        });

        assertEquals("Inventory is empty", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRemoveFromInventory_CardNotInInventory() {
        // Arrange
        testUser.getInventory().put(1, 5);
        when(userRepository.findById("testUser")).thenReturn(Optional.of(testUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.removeFromInventory("testUser", 25);
        });

        assertTrue(exception.getMessage().contains("Cannot remove"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetPlayerInventory_Success() {
        // Arrange
        Map<Integer, Integer> inventory = new HashMap<>();
        inventory.put(1, 2);
        inventory.put(25, 5);
        testUser.setInventory(inventory);
        when(userRepository.findById("testUser")).thenReturn(Optional.of(testUser));

        // Act
        Map<Integer, Integer> result = userService.getPlayerInventory("testUser");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2, result.get(1));
        assertEquals(5, result.get(25));
    }

    @Test
    void testGetPlayerInventory_UserNotFound() {
        // Arrange
        when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getPlayerInventory("nonexistent");
        });

        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void testSellCard_Success() {
        // Arrange
        testUser.getInventory().put(25, 2);
        testUser.setCoins(100);
        when(userRepository.findById("testUser")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.sellCard("testUser", 25, 20);

        // Assert
        assertNotNull(result);
        assertEquals(120, result.getCoins(), "Coins should increase by sell price");
        assertEquals(1, result.getInventory().get(25), "Card count should decrease by 1");
        verify(userRepository, times(2)).save(testUser); // Once in removeFromInventory, once in sellCard
    }

    @Test
    void testGetUserCoins() {
        // Arrange
        testUser.setCoins(150);
        when(userRepository.findById("testUser")).thenReturn(Optional.of(testUser));

        // Act
        int coins = userService.getUserCoins("testUser");

        // Assert
        assertEquals(150, coins);
    }

    @Test
    void testGetUser_Success() {
        // Arrange
        when(userRepository.findById("testUser")).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.getUser("testUser");

        // Assert
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void testGetUser_NotFound() {
        // Arrange
        when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUser("nonexistent");
        });

        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void testSaveUser() {
        // Arrange
        when(userRepository.save(testUser)).thenReturn(testUser);

        // Act
        User result = userService.saveUser(testUser);

        // Assert
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository).save(testUser);
    }

    @Test
    void testProcessBoosterEvent_Success() {
        // Arrange
        testUser.setUsername("defaultUser");
        testUser.getInventory().put(1, 1);
        when(userRepository.findById("defaultUser")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        String message = "1:Bulbasaur, 25:Pikachu, 4:Charmander";

        // Act
        userService.processBoosterEvent(message);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(2, savedUser.getInventory().get(1), "Bulbasaur count should increase");
        assertEquals(1, savedUser.getInventory().get(25), "Pikachu should be added");
        assertEquals(1, savedUser.getInventory().get(4), "Charmander should be added");
    }

    @Test
    void testProcessBoosterEvent_InvalidFormat() {
        // Arrange
        testUser.setUsername("defaultUser");
        when(userRepository.findById("defaultUser")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        String message = "invalid:format, 25:Pikachu, notanumber:Error";

        // Act - should not throw exception, just skip invalid entries
        userService.processBoosterEvent(message);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(1, savedUser.getInventory().get(25), "Valid entry should be processed");
        assertNull(savedUser.getInventory().get(0), "Invalid entries should be skipped");
    }

    @Test
    void testProcessBoosterEvent_DefaultUserNotFound() {
        // Arrange
        when(userRepository.findById("defaultUser")).thenReturn(Optional.empty());

        String message = "1:Bulbasaur";

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userService.processBoosterEvent(message);
        });

        verify(userRepository, never()).save(any(User.class));
    }
}

