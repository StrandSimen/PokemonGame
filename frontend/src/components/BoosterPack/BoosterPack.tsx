import React, { useState } from "react";
import "./BoosterPack.css";
import type { Card } from "../../types/Card";
import { API_ENDPOINTS } from "../../config/apiConfig";

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
            const res = await fetch(API_ENDPOINTS.BOOSTER_OPEN);
            const data = await res.json();

            if (!res.ok) {
                alert(data?.error || "Failed to fetch booster pack");
                return;
            }

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

                    <button className="open-button" onClick={() => handleNextCard()}>
                        Next
                    </button>
                </div>

            )}
        </div>
    );
};

export default BoosterPack;