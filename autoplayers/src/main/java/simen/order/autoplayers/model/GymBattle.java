package simen.order.autoplayers.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "gym_battles")
public class GymBattle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String trainerName;

    @Column(nullable = false)
    private String trainerType;

    @ElementCollection
    @CollectionTable(name = "battle_player_team", joinColumns = @JoinColumn(name = "battle_id"))
    @Column(name = "pokedex_number")
    private List<Integer> playerTeam;

    @ElementCollection
    @CollectionTable(name = "battle_trainer_team", joinColumns = @JoinColumn(name = "battle_id"))
    @Column(name = "pokedex_number")
    private List<Integer> trainerTeam;

    @Column(nullable = false)
    private boolean playerWon;

    @Column(nullable = false)
    private int coinsEarned;

    @Column
    private String badgeEarned;

    @Column(nullable = false)
    private LocalDateTime battleDate = LocalDateTime.now();

    @Column(length = 5000)
    private String battleLog;

    // Constructors
    public GymBattle() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getTrainerName() { return trainerName; }
    public void setTrainerName(String trainerName) { this.trainerName = trainerName; }

    public String getTrainerType() { return trainerType; }
    public void setTrainerType(String trainerType) { this.trainerType = trainerType; }

    public List<Integer> getPlayerTeam() { return playerTeam; }
    public void setPlayerTeam(List<Integer> playerTeam) { this.playerTeam = playerTeam; }

    public List<Integer> getTrainerTeam() { return trainerTeam; }
    public void setTrainerTeam(List<Integer> trainerTeam) { this.trainerTeam = trainerTeam; }

    public boolean isPlayerWon() { return playerWon; }
    public void setPlayerWon(boolean playerWon) { this.playerWon = playerWon; }

    public int getCoinsEarned() { return coinsEarned; }
    public void setCoinsEarned(int coinsEarned) { this.coinsEarned = coinsEarned; }

    public String getBadgeEarned() { return badgeEarned; }
    public void setBadgeEarned(String badgeEarned) { this.badgeEarned = badgeEarned; }

    public LocalDateTime getBattleDate() { return battleDate; }
    public void setBattleDate(LocalDateTime battleDate) { this.battleDate = battleDate; }

    public String getBattleLog() { return battleLog; }
    public void setBattleLog(String battleLog) { this.battleLog = battleLog; }
}

