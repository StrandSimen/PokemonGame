package simen.order.autoplayers.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import simen.order.autoplayers.dto.BattleRequest;
import simen.order.autoplayers.dto.BattleResult;
import simen.order.autoplayers.model.Card;
import simen.order.autoplayers.model.GymBattle;
import simen.order.autoplayers.model.GymTrainer;
import simen.order.autoplayers.repository.CardRepository;
import simen.order.autoplayers.repository.GymBattleRepository;

import java.util.*;

@Service
public class GymBattleService {

    @Autowired
    private GymTrainerService gymTrainerService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private GymBattleRepository gymBattleRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final Map<String, List<String>> TYPE_COUNTERS = new HashMap<>();
    private static final int BASE_COIN_REWARD = 50;
    private static final Map<String, String> BADGE_MAP = new HashMap<>();

    static {
        // Type counters - what beats what
        TYPE_COUNTERS.put("Fire", Arrays.asList("Grass", "Ice"));
        TYPE_COUNTERS.put("Water", Arrays.asList("Fire", "Ground", "Rock"));
        TYPE_COUNTERS.put("Grass", Arrays.asList("Water", "Ground", "Rock"));
        TYPE_COUNTERS.put("Electric", Arrays.asList("Water", "Flying"));
        TYPE_COUNTERS.put("Ice", Arrays.asList("Grass", "Ground", "Flying"));
        TYPE_COUNTERS.put("Fighting", Arrays.asList("Normal", "Rock", "Ice"));
        TYPE_COUNTERS.put("Poison", Arrays.asList("Grass"));
        TYPE_COUNTERS.put("Ground", Arrays.asList("Fire", "Electric", "Poison", "Rock"));
        TYPE_COUNTERS.put("Flying", Arrays.asList("Grass", "Fighting"));
        TYPE_COUNTERS.put("Psychic", Arrays.asList("Fighting", "Poison"));
        TYPE_COUNTERS.put("Rock", Arrays.asList("Fire", "Ice", "Flying"));
        TYPE_COUNTERS.put("Normal", new ArrayList<>());
        TYPE_COUNTERS.put("Colorless", new ArrayList<>());

        // Badge rewards
        BADGE_MAP.put("Blaine", "Volcano Badge");
        BADGE_MAP.put("Misty", "Cascade Badge");
        BADGE_MAP.put("Erika", "Rainbow Badge");
    }

    public BattleResult startBattle(BattleRequest request) {
        // Validate request
        if (request.getPlayerTeam() == null || request.getPlayerTeam().size() != 3) {
            throw new RuntimeException("Player must select exactly 3 Pokemon");
        }

        // Get trainer
        GymTrainer trainer = gymTrainerService.getTrainer(request.getTrainerName());

        // Get cards for both teams
        List<Card> playerCards = request.getPlayerTeam().stream()
                .map(id -> cardRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Pokemon with ID " + id + " not found")))
                .toList();
        List<Card> trainerCards = trainer.getPokemonTeam().stream()
                .map(id -> cardRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Pokemon with ID " + id + " not found")))
                .toList();

        if (playerCards.size() != 3) {
            throw new RuntimeException("Some of the player's Pokemon were not found in database");
        }

        // Simulate battle
        StringBuilder battleLog = new StringBuilder();
        boolean playerWon = simulateBattle(playerCards, trainerCards, trainer.getType(), battleLog);

        // Calculate rewards
        int coinsEarned = playerWon ? BASE_COIN_REWARD : 10;
        String badgeEarned = playerWon ? BADGE_MAP.get(trainer.getName()) : null;

        // Save battle record
        GymBattle battle = new GymBattle();
        battle.setUsername(request.getUsername());
        battle.setTrainerName(trainer.getName());
        battle.setTrainerType(trainer.getType());
        battle.setPlayerTeam(request.getPlayerTeam());
        battle.setTrainerTeam(trainer.getPokemonTeam());
        battle.setPlayerWon(playerWon);
        battle.setCoinsEarned(coinsEarned);
        battle.setBadgeEarned(badgeEarned);
        battle.setBattleLog(battleLog.toString());

        GymBattle savedBattle = gymBattleRepository.save(battle);

        // Award coins to user via RabbitMQ
        if (coinsEarned > 0) {
            awardCoinsToUser(request.getUsername(), coinsEarned);
        }

        return new BattleResult(playerWon, coinsEarned, badgeEarned, battleLog.toString(), savedBattle);
    }

    private boolean simulateBattle(List<Card> playerCards, List<Card> trainerCards, String trainerType, StringBuilder log) {
        log.append("=== BATTLE START ===\n\n");

        // Display starting teams
        log.append("Player's Team:\n");
        for (Card card : playerCards) {
            int hp = parseHP(card.getHp());
            log.append("  ").append(card.getName()).append(" (HP: ").append(hp).append(")\n");
        }
        log.append("\nVS\n\n");
        log.append("Trainer's Team:\n");
        for (Card card : trainerCards) {
            int hp = parseHP(card.getHp());
            log.append("  ").append(card.getName()).append(" (HP: ").append(hp).append(")\n");
        }

        log.append("\n=== BATTLE ROUNDS ===\n\n");

        Random random = new Random();
        int round = 1;

        int playerPokemonFainted = 0;
        int trainerPokemonFainted = 0;

        // Battle each pokemon
        for (int i = 0; i < 3; i++) {
            Card playerPokemon = playerCards.get(i);
            Card trainerPokemon = trainerCards.get(i);

            int playerHP = parseHP(playerPokemon.getHp());
            int trainerHP = parseHP(trainerPokemon.getHp());

            log.append("Round ").append(round++).append(": ").append(playerPokemon.getName())
                    .append(" vs ").append(trainerPokemon.getName()).append("\n");

            // Battle until one faints
            while (playerHP > 0 && trainerHP > 0) {
                // Player attacks
                int playerDamage = 15 + random.nextInt(20);
                String playerType = getFirstType(playerPokemon.getTypes());

                if (isCounter(playerType, trainerType)) {
                    playerDamage *= 2;
                    log.append("  ➜ ").append(playerPokemon.getName()).append(" attacks with SUPER EFFECTIVE ");
                    log.append(playerType).append(" move! Deals ").append(playerDamage).append(" damage!\n");
                } else {
                    log.append("  ➜ ").append(playerPokemon.getName()).append(" attacks! Deals ").append(playerDamage).append(" damage.\n");
                }
                trainerHP -= playerDamage;

                if (trainerHP <= 0) {
                    log.append("  ✓ ").append(trainerPokemon.getName()).append(" fainted!\n\n");
                    trainerPokemonFainted++;
                    break;
                }

                // Trainer attacks
                int trainerDamage = 15 + random.nextInt(20);
                if (isCounter(trainerType, playerType)) {
                    trainerDamage *= 2;
                    log.append("  ➜ ").append(trainerPokemon.getName()).append(" counters with SUPER EFFECTIVE ");
                    log.append(trainerType).append(" move! Deals ").append(trainerDamage).append(" damage!\n");
                } else {
                    log.append("  ➜ ").append(trainerPokemon.getName()).append(" attacks! Deals ").append(trainerDamage).append(" damage.\n");
                }
                playerHP -= trainerDamage;

                if (playerHP <= 0) {
                    log.append("  ✗ ").append(playerPokemon.getName()).append(" fainted!\n\n");
                    playerPokemonFainted++;
                    break;
                }
            }
        }

        // Determine winner based on who has more Pokemon standing
        boolean playerWon = trainerPokemonFainted > playerPokemonFainted;

        log.append("=== BATTLE END ===\n");
        log.append("Player's Pokemon fainted: ").append(playerPokemonFainted).append("/3\n");
        log.append("Trainer's Pokemon fainted: ").append(trainerPokemonFainted).append("/3\n\n");
        if (playerWon) {
            log.append("🎉 YOU WON! Congratulations!\n");
        } else {
            log.append("💔 YOU LOST! Better luck next time!\n");
        }

        return playerWon;
    }

    private int parseHP(String hp) {
        if (hp == null || hp.isEmpty()) {
            return 50; // Default HP
        }
        try {
            // Remove any non-numeric characters
            return Integer.parseInt(hp.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 50;
        }
    }

    private String getFirstType(String types) {
        if (types == null || types.isEmpty()) {
            return "Normal";
        }
        String[] typeArray = types.split(",");
        return typeArray[0].trim();
    }

    private boolean isCounter(String attackType, String defenseType) {
        List<String> counters = TYPE_COUNTERS.getOrDefault(attackType, new ArrayList<>());
        return counters.contains(defenseType);
    }

    private void awardCoinsToUser(String username, int coins) {
        try {
            String message = String.format("BATTLE_REWARD:%s:%d", username, coins);
            rabbitTemplate.convertAndSend("gym.queue", message);
            System.out.println("Sent coin reward message: " + message);
        } catch (Exception e) {
            System.err.println("Failed to send coin reward message: " + e.getMessage());
        }
    }

    public List<GymBattle> getBattleHistory(String username) {
        return gymBattleRepository.findByUsername(username);
    }

    public List<GymBattle> getWonBattles(String username) {
        return gymBattleRepository.findByUsernameAndPlayerWon(username, true);
    }
}

