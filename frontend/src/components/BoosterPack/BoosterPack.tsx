import React, { useState } from "react";
import "./BoosterPack.css";
import type { Card } from "../../types/Card";
import { API_ENDPOINTS } from "../../config/apiConfig";
import charizardPack from "../pictures/charizard-pack.webp";
import blastoisePack from "../pictures/blastoise.webp";
import venusaurPack from "../pictures/venasaur.webp";

interface BoosterPackProps {
    onCardsOpened?: (cards: Card[]) => void;
}

interface BoosterPackType {
    id: string;
    name: string;
    image: string;
    color: string;
}

const BOOSTER_PACKS: BoosterPackType[] = [
    { id: "charizard", name: "Charizard Pack", image: charizardPack, color: "#ff6b35" },
    { id: "blastoise", name: "Blastoise Pack", image: blastoisePack, color: "#4a90e2" },
    { id: "venusaur", name: "Venusaur Pack", image: venusaurPack, color: "#7cb342" },
];

const BoosterPack: React.FC<BoosterPackProps> = ({ onCardsOpened }) => {
    const [cards, setCards] = useState<Card[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const [currentIndex, setCurrentIndex] = useState(0);
    const [stage, setStage] = useState<"initial" | "selection" | "transitioning" | "opening" | "revealing">("initial");
    const [selectedPack, setSelectedPack] = useState<BoosterPackType | null>(null);

    const handleShowPacks = () => {
        setStage("selection");
    };

    const handleCancelSelection = () => {
        setStage("initial");
    };

    const handleSelectPack = (pack: BoosterPackType) => {
        setSelectedPack(pack);
        setStage("transitioning");

        // Short delay for fade-out effect, then start opening animation
        setTimeout(() => {
            setStage("opening");
            // After opening animation, fetch cards
            setTimeout(() => {
                openBoosterPack();
            }, 2000);
        }, 500);
    };

    const openBoosterPack = async () => {
        setLoading(true);
        setError(null);
        try {
            const res = await fetch(API_ENDPOINTS.BOOSTER_OPEN);
            const data = await res.json();

            if (!res.ok) {
                alert(data?.error || "Failed to fetch booster pack");
                setStage("initial");
                setSelectedPack(null);
                return;
            }

            setCards(data);
            setCurrentIndex(0);
            setStage("revealing");
        } catch (err) {
            setError((err as Error).message);
            setStage("initial");
            setSelectedPack(null);
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
    };

    const closePackopening = () => {
        setStage("initial");
        setSelectedPack(null);
        setCurrentIndex(0);
        // Notify parent with the cards that were opened
        if (onCardsOpened && cards.length > 0) {
            onCardsOpened(cards);
        }
    };

    return (
        <div className="booster-container">
            <h1>Open a Booster Pack</h1>
            <p className="booster-cost">Cost: 20 🪙</p>

            {/* Initial State - Show button to reveal packs */}
            {stage === "initial" && (
                <div className="initial-stage">
                    <button className="open-button reveal-button" onClick={handleShowPacks}>
                        Choose Your Pack!
                    </button>
                </div>
            )}

            {/* Selection Stage - Show 3 packs sliding in */}
            {stage === "selection" && (
                <div className="selection-stage" onClick={handleCancelSelection}>
                    <p className="selection-hint">Choose your booster pack!</p>
                    <div className="pack-selection" onClick={(e) => e.stopPropagation()}>
                        {BOOSTER_PACKS.map((pack, index) => (
                            <div
                                key={pack.id}
                                className={`pack-option pack-${index}`}
                                onClick={() => handleSelectPack(pack)}
                                style={{ animationDelay: `${index * 0.2}s` }}
                            >
                                <div className="pack-image-container">
                                    <img src={pack.image} alt={pack.name} className="pack-image" />
                                    <div className="pack-glow" style={{ backgroundColor: pack.color }}></div>
                                </div>
                                <p className="pack-name">{pack.name}</p>
                            </div>
                        ))}
                    </div>
                </div>
            )}

            {/* Transitioning Stage - Fade out non-selected packs */}
            {stage === "transitioning" && selectedPack && (
                <div className="selection-stage">
                    <p className="selection-hint fade-out">Choose your booster pack!</p>
                    <div className="pack-selection">
                        {BOOSTER_PACKS.map((pack) => (
                            <div
                                key={pack.id}
                                className={`pack-option ${pack.id === selectedPack.id ? 'selected-pack' : 'fade-out-pack'}`}
                            >
                                <div className="pack-image-container">
                                    <img src={pack.image} alt={pack.name} className="pack-image" />
                                    <div className="pack-glow" style={{ backgroundColor: pack.color }}></div>
                                </div>
                                <p className="pack-name">{pack.name}</p>
                            </div>
                        ))}
                    </div>
                </div>
            )}

            {/* Opening Stage - Pack opening animation */}
            {stage === "opening" && selectedPack && (
                <div className="opening-stage">
                    <div className="opening-pack-container">
                        <img
                            src={selectedPack.image}
                            alt={selectedPack.name}
                            className="opening-pack-image"
                        />
                        <div className="opening-effects">
                            <div className="spark spark-1"></div>
                            <div className="spark spark-2"></div>
                            <div className="spark spark-3"></div>
                            <div className="spark spark-4"></div>
                        </div>
                        <div className="opening-text">Opening...</div>
                    </div>
                </div>
            )}

            {/* Loading State */}
            {loading && stage === "revealing" && (
                <div className="loading-stage">
                    <div className="pokeball-loader"></div>
                    <p>Revealing your cards...</p>
                </div>
            )}

            {/* Error State */}
            {error && <p className="error-message">Error: {error}</p>}

            {/* Revealing Stage - Show cards one by one */}
            {stage === "revealing" && !loading && cards.length > 0 && (
                <div className="card-display">
                    <div className="card-counter">Card {currentIndex + 1} / {cards.length}</div>
                    <div className="card reveal-card">
                        <img src={cards[currentIndex].imageUrl} alt={cards[currentIndex].name} />
                    </div>
                    <button className="open-button next-button" onClick={() => handleNextCard()}>
                        {currentIndex < cards.length - 1 ? "Next Card ➡️" : "Finish ✓"}
                    </button>
                </div>

            )}
        </div>
    );
};

export default BoosterPack;