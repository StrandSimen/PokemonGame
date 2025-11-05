package simen.order.autoplayers.dto;

import simen.order.autoplayers.model.GymBattle;

public class BattleResult {
    private boolean playerWon;
    private int coinsEarned;
    private String badgeEarned;
    private String battleLog;
    private GymBattle battleRecord;

    public BattleResult() {}

    public BattleResult(boolean playerWon, int coinsEarned, String badgeEarned, String battleLog, GymBattle battleRecord) {
        this.playerWon = playerWon;
        this.coinsEarned = coinsEarned;
        this.badgeEarned = badgeEarned;
        this.battleLog = battleLog;
        this.battleRecord = battleRecord;
    }

    public boolean isPlayerWon() { return playerWon; }
    public void setPlayerWon(boolean playerWon) { this.playerWon = playerWon; }

    public int getCoinsEarned() { return coinsEarned; }
    public void setCoinsEarned(int coinsEarned) { this.coinsEarned = coinsEarned; }

    public String getBadgeEarned() { return badgeEarned; }
    public void setBadgeEarned(String badgeEarned) { this.badgeEarned = badgeEarned; }

    public String getBattleLog() { return battleLog; }
    public void setBattleLog(String battleLog) { this.battleLog = battleLog; }

    public GymBattle getBattleRecord() { return battleRecord; }
    public void setBattleRecord(GymBattle battleRecord) { this.battleRecord = battleRecord; }
}

