package simen.order.autoplayers.dto;

import java.util.List;

public class BattleRequest {
    private String username;
    private String trainerName;
    private List<Integer> playerTeam;

    public BattleRequest() {}

    public BattleRequest(String username, String trainerName, List<Integer> playerTeam) {
        this.username = username;
        this.trainerName = trainerName;
        this.playerTeam = playerTeam;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getTrainerName() { return trainerName; }
    public void setTrainerName(String trainerName) { this.trainerName = trainerName; }

    public List<Integer> getPlayerTeam() { return playerTeam; }
    public void setPlayerTeam(List<Integer> playerTeam) { this.playerTeam = playerTeam; }
}

