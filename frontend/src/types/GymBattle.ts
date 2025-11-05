export interface GymTrainer {
    name: string;
    type: string;
    pokemonTeam: number[];
}

export interface BattleRequest {
    username: string;
    trainerName: string;
    playerTeam: number[];
}

export interface BattleResult {
    playerWon: boolean;
    coinsEarned: number;
    badgeEarned: string | null;
    battleLog: string;
    battleRecord: {
        id: number;
        username: string;
        trainerName: string;
        trainerType: string;
        playerTeam: number[];
        trainerTeam: number[];
        playerWon: boolean;
        coinsEarned: number;
        badgeEarned: string | null;
        battleDate: string;
        battleLog: string;
    };
}

