package simen.order.boosterpack.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import simen.order.boosterpack.model.Card;
import simen.order.boosterpack.services.BoosterpackService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest(controllers = BoosterpackController.class)
@ActiveProfiles("test")
class BoosterpackControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private BoosterpackService boosterpackService;

    @Test
    void testOpenPack_Success() {
        // Arrange
        List<Card> mockCards = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Card card = new Card();
            card.setPokedexNumber(i);
            card.setName("Pokemon" + i);
            card.setHp("100");
            mockCards.add(card);
        }

        when(boosterpackService.openBooster()).thenReturn(mockCards);

        // Act & Assert
        webTestClient.get()
                .uri("/api/boosterpack/open")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(10)
                .jsonPath("$[0].pokedexNumber").isEqualTo(1)
                .jsonPath("$[0].name").isEqualTo("Pokemon1")
                .jsonPath("$[9].pokedexNumber").isEqualTo(10);

        verify(boosterpackService, times(1)).openBooster();
    }

    @Test
    void testOpenPack_NotEnoughCoins() {
        // Arrange
        when(boosterpackService.openBooster())
                .thenThrow(new RuntimeException("Not enough coins to open booster pack"));

        // Act & Assert
        webTestClient.get()
                .uri("/api/boosterpack/open")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Not enough coins to open booster pack");

        verify(boosterpackService, times(1)).openBooster();
    }

    @Test
    void testGetInstanceInfo() {
        // Act & Assert
        webTestClient.get()
                .uri("/api/boosterpack/info")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.service").exists()
                .jsonPath("$.hostname").exists()
                .jsonPath("$.port").exists();
    }
}

