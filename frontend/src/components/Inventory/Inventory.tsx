import React, { useEffect, useState } from "react";
import "./Inventory.css";
import type { Card } from "../../types/Card.ts";
import { useNavigate } from "react-router-dom";
import { API_ENDPOINTS } from "../../config/apiConfig";
import BadgeDisplay from "../Badge/BadgeDisplay";

interface CardWithCount extends Card {
    count: number;
}

const Inventory: React.FC = () => {
    const [inventory, setInventory] = useState<CardWithCount[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [selectedCard, setSelectedCard] = useState<CardWithCount | null>(null);
    const navigate = useNavigate();

    const fetchInventory = async () => {
        try {
            setLoading(true);
            setError(null);
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
        fetchInventory();
    }, []);

    const handleCardClick = (card: CardWithCount) => {
        setSelectedCard(card);
    };

    const handleCloseModal = () => {
        setSelectedCard(null);
    };

    const handleSellCard = async (cardId: number) => {
        try {
            const res = await fetch(API_ENDPOINTS.USER_SELL(cardId, 20), {
                method: "POST",
            });
            if (!res.ok) throw new Error("Failed to sell card");

            console.log(`Sold card ${cardId} for 20 coins`);

            // Close modal and refresh inventory
            setSelectedCard(null);
            await fetchInventory();
        } catch (err) {
            console.error(err);
            alert("Failed to sell card");
        }
    };

    if (loading) return <div className="inventory-loading">Loading inventory...</div>;
    if (error) return <div className="inventory-error">Error: {error}</div>;

    // Create array with duplicates for display
    const cardsToDisplay: CardWithCount[] = [];
    inventory.forEach(card => {
        for (let i = 0; i < card.count; i++) {
            cardsToDisplay.push(card);
        }
    });

    return (
        <div className="inventory-container">
            <button onClick={() => navigate("/")} className="back-button">← Back to Home</button>

            <div className="inventory-content">
                <h2 className="inventory-title">Your Pokémon Collection</h2>

                <BadgeDisplay variant="full" />

                <p className="inventory-count">Total Cards: {cardsToDisplay.length}</p>

                {cardsToDisplay.length === 0 ? (
                    <div className="empty-inventory">
                        <p>Your collection is empty!</p>
                        <p>Open some booster packs to get started.</p>
                    </div>
                ) : (
                    <div className="card-inventory-grid">
                        {cardsToDisplay.map((card, index) => (
                            <div
                                key={`${card.pokedexNumber}-${index}`}
                                className="inventory-card"
                                onClick={() => handleCardClick(card)}
                            >
                                <img src={card.imageUrl} alt={card.name} />
                                <div className="card-name">{card.name}</div>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {/* Modal for selected card */}
            {selectedCard && (
                <div className="card-modal-overlay" onClick={handleCloseModal}>
                    <div className="card-modal" onClick={(e) => e.stopPropagation()}>
                        <button className="close-modal" onClick={handleCloseModal}>×</button>
                        <div className="modal-content">
                            <img src={selectedCard.imageUrl} alt={selectedCard.name} className="modal-card-image" />
                            <div className="modal-card-info">
                                <h2>{selectedCard.name}</h2>
                                <p className="card-detail"><strong>HP:</strong> {selectedCard.hp}</p>
                                <p className="card-detail"><strong>Type:</strong> {selectedCard.types}</p>
                                <p className="card-detail"><strong>Pokédex #:</strong> {selectedCard.pokedexNumber}</p>
                                <p className="card-count-info">You own {selectedCard.count} of this card</p>
                                <button
                                    className="sell-button-modal"
                                    onClick={() => handleSellCard(selectedCard.pokedexNumber)}
                                >
                                    Sell for 20 coins
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Inventory;
