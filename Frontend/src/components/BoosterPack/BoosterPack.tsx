import React, { useState } from "react";
import type { Card } from "../../types/Card";
import "./BoosterPack.css";

const BoosterPack: React.FC = () => {
    const [cards, setCards] = useState<Card[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const [currentIndex, setCurrentIndex] = useState(0);
    const [opened, setOpened] = useState(false);

    const handleOpenPack = async () => {
        setLoading(true);
        setError(null);
        try {
            const res = await fetch("http://localhost:8080/api/boosterpack/open");
            if (!res.ok) throw new Error("Failed to fetch booster pack");
            const data: Card[] = await res.json();
            setCards(data);
            setCurrentIndex(0);
            setOpened(true);
        } catch (err) {
            setError((err as Error).message);
        } finally {
            setLoading(false);
        }
    };

    const handleNextCard = () => {
        if (currentIndex < cards.length - 1) {
            setCurrentIndex((prev) => prev + 1);
        } else {
            closePackopening();
        }
    }

    const handleAddToInventory = async (card: Card) => {
        try {
            const res = await fetch(`http://localhost:8081/api/defaultUser/add/${card.pokedexNumber}`, {
                method : "POST"
            });
            if (!res.ok) throw new Error("Failed to add to inventory");
            console.log("Added Card", card.pokedexNumber);

            handleNextCard();
        } catch (err) {
            console.error(err);
        }
    }

    const handleSellCard = async ()=> {
        try {
            const res = await fetch(`http://localhost:8081/api/defaultUser/sell`,
                {method: "POST"}
            );
            if (!res) throw new Error("Failed to sell card");
            console.log("Sold card for 20 coins")

            handleNextCard();
        } catch (err) {
            console.log(err);
        }
    }

    const closePackopening = () => {
        setOpened(false);
        setCurrentIndex(0);
    }

    return (
        <div className="booster-container">
            <h1>Open a Booster Pack</h1>

            {!opened && (
                <>
                    <div className="booster-image">
                    </div>
                    <button className="open-button" onClick={handleOpenPack}>
                        Open Pack
                    </button>
                </>
            )}

            {loading && <p>Loading...</p>}
            {error && <p>Error: {error}</p>}

            {opened && cards.length > 0 && (
                <div className="card-display">
                    <div className="card">
                        <img src={cards[currentIndex].imageUrl}/>
                    </div>

                    <button className="open-button" onClick={() => handleAddToInventory(cards[currentIndex])}>
                        Add to Inventory
                    </button>

                    <button className="open-button" onClick={() => handleSellCard()}>
                        Sell Card
                    </button>
                </div>


            )}
        </div>
    );
};

export default BoosterPack;