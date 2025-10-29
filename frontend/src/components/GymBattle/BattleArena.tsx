import React, { useEffect, useState } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import type { BattleResult } from "../../types/GymBattle";
import "./BattleArena.css";
import { API_ENDPOINTS } from "../../config/apiConfig";

const BattleArena: React.FC = () => {
    const { trainerName } = useParams<{ trainerName: string }>();
    const location = useLocation();
    const navigate = useNavigate();
    const playerTeam = location.state?.playerTeam as number[] | undefined;

    const [battleResult, setBattleResult] = useState<BattleResult | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [showLog, setShowLog] = useState(false);

    useEffect(() => {
        if (!playerTeam || playerTeam.length !== 3) {
            navigate("/gym-battle");
            return;
        }

        const startBattle = async () => {
            try {
                const res = await fetch(API_ENDPOINTS.GYM_BATTLE, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        username: "defaultUser",
                        trainerName: trainerName,
                        playerTeam: playerTeam,
                    }),
                });

                if (!res.ok) {
                    const errorData = await res.json();
                    throw new Error(errorData.error || "Failed to start battle");
                }

                const data: BattleResult = await res.json();
                setBattleResult(data);
            } catch (err) {
                setError((err as Error).message);
            } finally {
                setLoading(false);
            }
        };

        startBattle();
    }, [playerTeam, trainerName, navigate]);

    if (loading) {
        return (
            <div className="battle-arena-container">
                <div className="loading-screen">
                    <h1>⚔️ Battle Starting...</h1>
                    <div className="loading-spinner"></div>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="battle-arena-container">
                <div className="error-screen">
                    <h1>Error</h1>
                    <p>{error}</p>
                    <button className="home-button" onClick={() => navigate("/gym-battle")}>
                        Back to Gym
                    </button>
                </div>
            </div>
        );
    }

    if (!battleResult) {
        return (
            <div className="battle-arena-container">
                <p>No battle result available</p>
            </div>
        );
    }

    return (
        <div className="battle-arena-container">
            <div className={`result-screen ${battleResult.playerWon ? "victory" : "defeat"}`}>
                {battleResult.playerWon ? (
                    <>
                        <h1 className="result-title victory-title">🎉 VICTORY! 🎉</h1>
                        <p className="result-message">You defeated {trainerName}!</p>
                        {battleResult.badgeEarned && (
                            <div className="badge-earned">
                                <p className="badge-title">You earned:</p>
                                <p className="badge-name">{battleResult.badgeEarned}</p>
                            </div>
                        )}
                    </>
                ) : (
                    <>
                        <h1 className="result-title defeat-title">💔 DEFEAT 💔</h1>
                        <p className="result-message">{trainerName} was too strong!</p>
                        <p className="encouragement">Train harder and come back!</p>
                    </>
                )}

                <div className="coins-earned">
                    <p className="coins-label">Coins Earned:</p>
                    <p className="coins-amount">+{battleResult.coinsEarned} 🪙</p>
                </div>

                <div className="battle-log-section">
                    <button
                        className="toggle-log-button"
                        onClick={() => setShowLog(!showLog)}
                    >
                        {showLog ? "Hide Battle Log" : "Show Battle Log"}
                    </button>

                    {showLog && (
                        <div className="battle-log">
                            <pre>{battleResult.battleLog}</pre>
                        </div>
                    )}
                </div>

                <div className="action-buttons-result">
                    <button className="rematch-button" onClick={() => navigate(`/gym-battle/setup/${trainerName}`)}>
                        Rematch
                    </button>
                    <button className="choose-trainer-button" onClick={() => navigate("/gym-battle")}>
                        Choose Another Trainer
                    </button>
                    <button className="home-button" onClick={() => navigate("/")}>
                        Back to Home
                    </button>
                </div>
            </div>
        </div>
    );
};

export default BattleArena;
