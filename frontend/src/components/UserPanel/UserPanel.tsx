import React, {useEffect, useState} from "react";
import type {Card} from "../../types/Card.ts";
import "./UserPanel.css";
import ashKetchum from "../pictures/ashKetchum.png"
import { useNavigate } from "react-router-dom";
import { API_ENDPOINTS } from "../../config/apiConfig";
import BadgeDisplay from "../Badge/BadgeDisplay";

interface CardWithCount extends Card {
    count: number;
}

interface UserPanelProps {
    recentCards?: Card[];
}

const UserPanel: React.FC<UserPanelProps> = ({ recentCards = [] }) => {
    const[coins, setCoins] = useState<number>(0);
    const[inventory, setInventory] = useState<CardWithCount[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const navigate = useNavigate();
    const fetchUserData = async () => {
        try {
            setLoading(true);
            setError(null);

            // Fetch coins
            const coinsRes = await fetch(API_ENDPOINTS.USER_COINS);
            if (!coinsRes.ok) throw new Error("Failed to fetch user coins");
            const coinsData = await coinsRes.json();
            setCoins(Number(coinsData));

            // Fetch inventory
            const invRes = await fetch(API_ENDPOINTS.USER_INVENTORY);
            if (!invRes.ok) throw new Error("Failed to fetch inventory");
            const invData: Record<string, number> = await invRes.json();

            const cards: CardWithCount[] = await Promise.all(
                Object.entries(invData).map(async ([id, count]) => {
                    const res = await fetch(API_ENDPOINTS.CARD_BY_ID(Number(id)));
                    if (!res.ok) throw new Error(`Failed to fetch card ${id}`);
                    const cardData = await res.json() as Card;
                    return { ...cardData, count };
                })
            );

            setInventory(cards);

        } catch (err) {
            setError((err as Error).message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUserData();
    }, []);

    // Refresh coins when recent cards change (pack was opened)
    useEffect(() => {
        if (recentCards.length > 0) {
            fetchUserData();
        }
    }, [recentCards]);

    if (loading) return <p>Loading user data...</p>;
    if (error) return <p>Error: {error}</p>;

    // Determine which cards to display
    let cardsToDisplay: Card[] = [];

    if (recentCards.length > 0) {
        // Show last 3 cards from the recently opened pack
        cardsToDisplay = recentCards.slice(-3);
    } else {
        // Show first 3 cards from inventory if no recent cards
        const allCards: Card[] = [];
        inventory.forEach(card => {
            for (let i = 0; i < card.count; i++) {
                allCards.push(card);
            }
        });
        cardsToDisplay = allCards.slice(0, 3);
    }

    return(
        <div className="user-panel">
            <h2>Ash Ketchum</h2>
            <img src={ashKetchum} alt="ashKetchum picture" className="profile-picture"/>
            <p>🪙: {coins}</p>

            <BadgeDisplay variant="compact" />

            <div>
                <h3>Inventory Preview:</h3>
                {cardsToDisplay.length > 0 ? (
                    <div className="card-grid">
                        {cardsToDisplay.map((card, index) => (
                            <div key={`${card.pokedexNumber}-${index}`} className="user-card">
                                <img src={card.imageUrl} alt={card.name}/>
                            </div>
                        ))}
                    </div>
                ) : (
                    <p>No cards in inventory yet.</p>
                )}
            </div>

            <button className="open-button" onClick={() => navigate("/inventory")}>
                Open inventory
            </button>
        </div>
    )
};

export default UserPanel