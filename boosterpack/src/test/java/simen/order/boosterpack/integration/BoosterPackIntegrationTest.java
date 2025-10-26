package simen.order.boosterpack.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import simen.order.boosterpack.config.TestRabbitConfig;
import simen.order.boosterpack.model.Card;
import simen.order.boosterpack.repository.CardRepo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Import(TestRabbitConfig.class)
class BoosterPackIntegrationTest {

    @Autowired
    private CardRepo cardRepo;

    @Test
    void testCardRepository_SaveAndFind() {
        // Arrange
        Card card = new Card();
        card.setPokedexNumber(999);
        card.setName("TestPokemon");
        card.setHp("100");
        card.setTypes("Fire");
        card.setImageUrl("http://example.com/image.png");

        // Act
        Card saved = cardRepo.save(card);
        Card found = cardRepo.findById(999).orElse(null);

        // Assert
        assertNotNull(saved);
        assertNotNull(found);
        assertEquals("TestPokemon", found.getName());
        assertEquals("Fire", found.getTypes());

        // Cleanup
        cardRepo.deleteById(999);
    }

    @Test
    void testCardRepository_FindAllOrdered() {
        // Arrange
        Card card1 = createCard(500, "Pokemon500");
        Card card2 = createCard(501, "Pokemon501");
        Card card3 = createCard(502, "Pokemon502");

        cardRepo.save(card3);
        cardRepo.save(card1);
        cardRepo.save(card2);

        // Act
        List<Card> cards = cardRepo.findAllByOrderByPokedexNumberAsc();

        // Assert
        assertNotNull(cards);
        assertTrue(cards.size() >= 3);

        // Find our test cards
        Card foundCard1 = cards.stream().filter(c -> c.getPokedexNumber() == 500).findFirst().orElse(null);
        Card foundCard2 = cards.stream().filter(c -> c.getPokedexNumber() == 501).findFirst().orElse(null);
        Card foundCard3 = cards.stream().filter(c -> c.getPokedexNumber() == 502).findFirst().orElse(null);

        assertNotNull(foundCard1);
        assertNotNull(foundCard2);
        assertNotNull(foundCard3);

        // Verify ordering
        int index1 = cards.indexOf(foundCard1);
        int index2 = cards.indexOf(foundCard2);
        int index3 = cards.indexOf(foundCard3);

        assertTrue(index1 < index2);
        assertTrue(index2 < index3);

        // Cleanup
        cardRepo.deleteById(500);
        cardRepo.deleteById(501);
        cardRepo.deleteById(502);
    }

    private Card createCard(int pokedexNumber, String name) {
        Card card = new Card();
        card.setPokedexNumber(pokedexNumber);
        card.setName(name);
        card.setHp("100");
        card.setTypes("Normal");
        return card;
    }
}

