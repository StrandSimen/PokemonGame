package simen.order.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import simen.order.user.model.User;
import simen.order.user.service.UserService;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebFluxTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setCoins(100);
        Map<Integer, Integer> inventory = new HashMap<>();
        inventory.put(1, 2);
        inventory.put(25, 5);
        testUser.setInventory(inventory);
    }

    @Test
    void testGetPlayerInventory() {
        when(userService.getPlayerInventory("testUser")).thenReturn(testUser.getInventory());

        webTestClient.get()
                .uri("/api/testUser/inventory")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.1").isEqualTo(2)
                .jsonPath("$.25").isEqualTo(5);

        verify(userService).getPlayerInventory("testUser");
    }

    @Test
    void testGetUserCoins() {
        when(userService.getUserCoins("testUser")).thenReturn(100);

        webTestClient.get()
                .uri("/api/testUser/coins")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .isEqualTo(100);

        verify(userService).getUserCoins("testUser");
    }

    @Test
    void testAddToInventory() {
        when(userService.addToInventory("testUser", 25)).thenReturn(testUser);

        webTestClient.post()
                .uri("/api/testUser/add/25")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("testUser")
                .jsonPath("$.coins").isEqualTo(100);

        verify(userService).addToInventory("testUser", 25);
    }

    @Test
    void testRemoveFromInventory() {
        when(userService.removeFromInventory("testUser", 25)).thenReturn(testUser);

        webTestClient.post()
                .uri("/api/testUser/remove/25")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("testUser");

        verify(userService).removeFromInventory("testUser", 25);
    }

    @Test
    void testSellCard_WithDefaultPrice() {
        when(userService.sellCard("testUser", 25, 20)).thenReturn(testUser);

        webTestClient.post()
                .uri("/api/testUser/sell/25")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("testUser");

        verify(userService).sellCard("testUser", 25, 20);
    }

    @Test
    void testSellCard_WithCustomPrice() {
        when(userService.sellCard("testUser", 25, 50)).thenReturn(testUser);

        webTestClient.post()
                .uri("/api/testUser/sell/25?sellPrice=50")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("testUser");

        verify(userService).sellCard("testUser", 25, 50);
    }

    @Test
    void testSpendCoins_Success() {
        testUser.setCoins(100);
        when(userService.getUser("defaultUser")).thenReturn(testUser);
        when(userService.saveUser(any(User.class))).thenReturn(testUser);

        webTestClient.post()
                .uri("/api/defaultUser/spendCoins")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Coins deducted");

        verify(userService).getUser("defaultUser");
        verify(userService).saveUser(any(User.class));
    }

    @Test
    void testSpendCoins_NotEnoughCoins() {
        testUser.setCoins(10);
        when(userService.getUser("defaultUser")).thenReturn(testUser);

        webTestClient.post()
                .uri("/api/defaultUser/spendCoins")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Not enough coins");

        verify(userService).getUser("defaultUser");
        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    void testGetInstanceInfo() {
        webTestClient.get()
                .uri("/api/info")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.service").exists()
                .jsonPath("$.hostname").exists()
                .jsonPath("$.port").exists();
    }
}

