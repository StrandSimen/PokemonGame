import React, { useState } from "react";
import BoosterPack from "../BoosterPack/BoosterPack.tsx";
import UserPanel from "../UserPanel/UserPanel.tsx";
import { useNavigate } from "react-router-dom";
import "./MainLayout.css";
import type { Card } from "../../types/Card";


const MainLayout: React.FC = () => {
    const navigate = useNavigate();
    const [recentCards, setRecentCards] = useState<Card[]>([]);

    const handleCardsOpened = (cards: Card[]) => {
        // Store the cards that were just opened
        setRecentCards(cards);
    };

    return (
        <div className="main-layout">
            <div className="header-panel">
                <h1 className="game-title">Pokémon Trading Card Game</h1>
                <button className="battle-button" onClick={() => navigate("/gym-battle")}>
                    Gym Battle
                </button>
            </div>
            <div className="content-panels">
                <div className="left-panel">
                    <BoosterPack onCardsOpened={handleCardsOpened}/>
                </div>
                <div className="right-panel">
                    <UserPanel recentCards={recentCards}/>
                </div>
            </div>
        </div>
    );
};

export default MainLayout;