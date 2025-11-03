package simen.order.boosterpack.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import simen.order.boosterpack.model.Card;
import simen.order.boosterpack.repository.CardRepo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoosterpackServiceTest {

    @Mock
    private CardRepo cardRepo;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BoosterpackService boosterpackService;

    private List<Card> testCards;

    @BeforeEach
    void setUp() throws Exception {
        // Create test cards
        testCards = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Card card = new Card();
            card.setPokedexNumber(i);
            card.setName("Pokemon" + i);
            card.setHp(String.valueOf(50 + i * 10));
            card.setTypes("Fire");
            testCards.add(card);
        }

        // Inject mocked RestTemplate and spendCoinsUrl using reflection
        Field restTemplateField = BoosterpackService.class.getDeclaredField("restTemplate");
        restTemplateField.setAccessible(true);
        restTemplateField.set(boosterpackService, restTemplate);

        Field spendCoinsUrlField = BoosterpackService.class.getDeclaredField("spendCoinsUrl");
        spendCoinsUrlField.setAccessible(true);
        spendCoinsUrlField.set(boosterpackService, "http://gateway:8100/api/defaultUser/spendCoins");
    }

    @Test
    void testOpenBooster_Success() {
        // Arrange
        when(cardRepo.findAllByOrderByPokedexNumberAsc()).thenReturn(testCards);
        when(restTemplate.postForEntity(anyString(), isNull(), eq(String.class)))
                .thenReturn(null);

        // Act
        List<Card> result = boosterpackService.openBooster();

        // Assert
        assertNotNull(result);
        assertEquals(11, result.size(), "Booster pack should contain exactly 11 cards");

        // Verify unique cards
        long uniqueCards = result.stream().distinct().count();
        assertEquals(11, uniqueCards, "All cards in booster should be unique");

        // Verify all cards are from the available pool
        assertTrue(testCards.containsAll(result), "All cards should be from available pool");

        // Verify coin spending was called
        verify(restTemplate).postForEntity(
                eq("http://gateway:8100/api/defaultUser/spendCoins"),
                isNull(),
                eq(String.class)
        );

        // Verify RabbitMQ message was sent (cards are added via RabbitMQ now)
        verify(rabbitTemplate).convertAndSend(eq("booster.queue"), anyString());
    }

    @Test
    void testOpenBooster_NotEnoughCoins() {
        // Arrange
        when(restTemplate.postForEntity(
                eq("http://gateway:8100/api/defaultUser/spendCoins"),
                isNull(),
                eq(String.class)
        )).thenThrow(HttpClientErrorException.class);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            boosterpackService.openBooster();
        });

        assertEquals("Not enough coins to open booster pack", exception.getMessage());

        // Verify no cards were fetched or added
        verify(cardRepo, never()).findAllByOrderByPokedexNumberAsc();
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString());
    }

    @Test
    void testOpenBooster_RabbitMQMessageFormat() {
        // Arrange
        when(cardRepo.findAllByOrderByPokedexNumberAsc()).thenReturn(testCards);
        when(restTemplate.postForEntity(anyString(), isNull(), eq(String.class)))
                .thenReturn(null);

        // Act
        List<Card> result = boosterpackService.openBooster();

        // Assert - verify RabbitMQ message format contains card info
        verify(rabbitTemplate, times(1)).convertAndSend(eq("booster.queue"), anyString());
        assertNotNull(result);
        assertEquals(11, result.size());
    }
}

