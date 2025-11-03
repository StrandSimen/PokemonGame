// API Configuration
const API_BASE_URL = "http://localhost:8100";

export const API_ENDPOINTS = {
    // User endpoints
    USER_COINS: `${API_BASE_URL}/api/defaultUser/coins`,
    USER_INVENTORY: `${API_BASE_URL}/api/defaultUser/inventory`,
    USER_SELL: (cardId: number, sellPrice: number) =>
        `${API_BASE_URL}/api/defaultUser/sell/${cardId}?sellPrice=${sellPrice}`,

    // Card endpoints
    CARD_BY_ID: (id: number) => `${API_BASE_URL}/api/cards/${id}`,

    // Booster pack endpoints
    BOOSTER_OPEN: `${API_BASE_URL}/api/boosterpack/open`,

    // Gym/Battle endpoints
    GYM_TRAINERS: `${API_BASE_URL}/api/gym/trainers`,
    GYM_TRAINER_BY_NAME: (trainerName: string) =>
        `${API_BASE_URL}/api/gym/trainers/${trainerName}`,
    GYM_BATTLE: `${API_BASE_URL}/api/gym/battle`,
    GYM_BADGES: (username: string) =>
        `${API_BASE_URL}/api/gym/badges/${username}`,
    GYM_BADGE_PROGRESS: (username: string) =>
        `${API_BASE_URL}/api/gym/badges/${username}/progress`,
};

