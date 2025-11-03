import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import type { GymTrainer } from "../../types/GymBattle";
import type { Card } from "../../types/Card";
import "./TrainerSelection.css";
import { API_ENDPOINTS } from "../../config/apiConfig";
import blaineImg from "../pictures/blaine.webp";
import mistyImg from "../pictures/misty.png";
import erikaImg from "../pictures/erika.png";

const TrainerSelection: React.FC = () => {
    const [trainers, setTrainers] = useState<Record<string, GymTrainer>>({});
    const [selectedTrainer, setSelectedTrainer] = useState<GymTrainer | null>(null);
    const [trainerCards, setTrainerCards] = useState<Card[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchTrainers = async () => {
            try {
                setLoading(true);
                const res = await fetch(API_ENDPOINTS.GYM_TRAINERS);
                if (!res.ok) throw new Error("Failed to fetch trainers");
                const data: Record<string, GymTrainer> = await res.json();
                setTrainers(data);
            } catch (err) {
                setError((err as Error).message);
            } finally {
                setLoading(false);
            }
        };

        fetchTrainers();
    }, []);

    const handleSelectTrainer = async (trainer: GymTrainer) => {
        setSelectedTrainer(trainer);

        // Fetch trainer's Pokemon cards
        try {
            const cards: Card[] = await Promise.all(
                trainer.pokemonTeam.map(async (id) => {
                    const res = await fetch(API_ENDPOINTS.CARD_BY_ID(id));
                    if (!res.ok) throw new Error(`Failed to fetch card ${id}`);
                    return res.json() as Promise<Card>;
                })
            );
            setTrainerCards(cards);
        } catch (err) {
            console.error("Failed to fetch trainer cards:", err);
        }
    };

    const handleBackToSelection = () => {
        setSelectedTrainer(null);
        setTrainerCards([]);
    };

    const handleProceedToBattle = () => {
        if (selectedTrainer) {
            navigate(`/gym-battle/setup/${selectedTrainer.name}`);
        }
    };

    const getTrainerColor = (type: string) => {
        const colors: Record<string, string> = {
            Fire: "linear-gradient(145deg, #f87171, #fb923c)",
            Water: "linear-gradient(145deg, #06b6d4, #22d3ee)",
            Grass: "linear-gradient(145deg, #4ade80, #22c55e)",
        };
        return colors[type] || "linear-gradient(145deg, #94a3b8, #64748b)";
    };

    const getTrainerBadge = (name: string) => {
        const badges: Record<string, string> = {
            Blaine: "🔥 Volcano Badge",
            Misty: "💧 Cascade Badge",
            Erika: "🌸 Rainbow Badge",
        };
        return badges[name] || "Badge";
    };

    const getTrainerImage = (name: string) => {
        const images: Record<string, string> = {
            Blaine: blaineImg,
            Misty: mistyImg,
            Erika: erikaImg,
        };
        return images[name];
    };

    if (loading) return <div className="gym-container"><p>Loading trainers...</p></div>;
    if (error) return <div className="gym-container"><p>Error: {error}</p></div>;

    return (
        <div className="gym-container">
            <button className="back-button" onClick={() => navigate("/")}>
                ← Back to Home
            </button>

            {!selectedTrainer ? (
                <>
                    <h1>Choose Your Opponent</h1>
                    <div className="trainer-grid">
                        {Object.values(trainers).map((trainer) => (
                            <div
                                key={trainer.name}
                                className="trainer-card"
                                style={{ background: getTrainerColor(trainer.type) }}
                                onClick={() => handleSelectTrainer(trainer)}
                            >
                                {getTrainerImage(trainer.name) && (
                                    <img
                                        src={getTrainerImage(trainer.name)}
                                        alt={trainer.name}
                                        className="trainer-profile-pic"
                                    />
                                )}
                                <h2>{trainer.name}</h2>
                                <p className="trainer-type">{trainer.type} Type Trainer</p>
                                <p className="trainer-badge">{getTrainerBadge(trainer.name)}</p>
                                <button className="select-button">Challenge!</button>
                            </div>
                        ))}
                    </div>
                </>
            ) : (
                <div className="trainer-detail">
                    {getTrainerImage(selectedTrainer.name) && (
                        <img
                            src={getTrainerImage(selectedTrainer.name)}
                            alt={selectedTrainer.name}
                            className="trainer-detail-profile-pic"
                        />
                    )}
                    <h1>Gym Leader: {selectedTrainer.name}</h1>
                    <p className="detail-type">{selectedTrainer.type} Type Specialist</p>
                    <p className="detail-badge">Win to earn: {getTrainerBadge(selectedTrainer.name)}</p>

                    <h3>Trainer's Team:</h3>
                    <div className="trainer-team-grid">
                        {trainerCards.map((card) => (
                            <div key={card.pokedexNumber} className="trainer-pokemon-card">
                                <img src={card.imageUrl} alt={card.name} />
                                <p className="card-name">{card.name}</p>
                                <p className="card-hp">HP: {card.hp}</p>
                            </div>
                        ))}
                    </div>

                    <div className="action-buttons">
                        <button className="cancel-button" onClick={handleBackToSelection}>
                            Choose Different Trainer
                        </button>
                        <button className="proceed-button" onClick={handleProceedToBattle}>
                            Select Your Team →
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default TrainerSelection;

