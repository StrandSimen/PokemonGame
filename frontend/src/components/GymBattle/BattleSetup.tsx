import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import type { GymTrainer } from "../../types/GymBattle";
import type { Card } from "../../types/Card";
import "./BattleSetup.css";
import { API_ENDPOINTS } from "../../config/apiConfig";
import blaineImg from "../pictures/blaine.webp";
import mistyImg from "../pictures/misty.png";
import erikaImg from "../pictures/erika.png";

const BattleSetup: React.FC = () => {
    const { trainerName } = useParams<{ trainerName: string }>();
    const [trainer, setTrainer] = useState<GymTrainer | null>(null);
    const [trainerCards, setTrainerCards] = useState<Card[]>([]);
    const [inventory, setInventory] = useState<Card[]>([]);
    const [selectedPokemon, setSelectedPokemon] = useState<number[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);

                // Fetch trainer data
                const trainerRes = await fetch(API_ENDPOINTS.GYM_TRAINER_BY_NAME(trainerName!));
                if (!trainerRes.ok) throw new Error("Failed to fetch trainer");
                const trainerData: GymTrainer = await trainerRes.json();
                setTrainer(trainerData);

                // Fetch trainer's cards
                const tCards: Card[] = await Promise.all(
                    trainerData.pokemonTeam.map(async (id) => {
                        const res = await fetch(API_ENDPOINTS.CARD_BY_ID(id));
                        if (!res.ok) throw new Error(`Failed to fetch card ${id}`);
                        return res.json() as Promise<Card>;
                    })
                );
                setTrainerCards(tCards);

                const invRes = await fetch(API_ENDPOINTS.USER_INVENTORY);
                if (!invRes.ok) throw new Error("Failed to fetch inventory");
                const invData: Record<string, number> = await invRes.json();

                const cards: Card[] = await Promise.all(
                    Object.keys(invData).map(async (id) => {
                        const res = await fetch(API_ENDPOINTS.CARD_BY_ID(Number(id)));
                        if (!res.ok) throw new Error(`Failed to fetch card ${id}`);
                        return res.json() as Promise<Card>;
                    })
                );
                setInventory(cards);
            } catch (err) {
                setError((err as Error).message);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [trainerName]);

    const handleSelectPokemon = (pokedexNumber: number) => {
        if (selectedPokemon.includes(pokedexNumber)) {
            // Remove from selection
            setSelectedPokemon(selectedPokemon.filter((id) => id !== pokedexNumber));
        } else if (selectedPokemon.length < 3) {
            // Add to selection (maintains order)
            setSelectedPokemon([...selectedPokemon, pokedexNumber]);
        }
    };

    const getOrderNumber = (pokedexNumber: number): number | null => {
        const index = selectedPokemon.indexOf(pokedexNumber);
        return index >= 0 ? index + 1 : null;
    };

    const getSelectedCards = (): Card[] => {
        return selectedPokemon.map(id =>
            inventory.find(card => card.pokedexNumber === id)
        ).filter(card => card !== undefined) as Card[];
    };

    const getTrainerImage = (name: string) => {
        const images: Record<string, string> = {
            Blaine: blaineImg,
            Misty: mistyImg,
            Erika: erikaImg,
        };
        return images[name];
    };

    const handleStartBattle = () => {
        if (selectedPokemon.length === 3 && trainer) {
            navigate(`/gym-battle/arena/${trainerName}`, {
                state: { playerTeam: selectedPokemon }
            });
        }
    };

    if (loading) return <div className="battle-setup-container"><p>Loading...</p></div>;
    if (error) return <div className="battle-setup-container"><p>Error: {error}</p></div>;
    if (!trainer) return <div className="battle-setup-container"><p>Trainer not found</p></div>;

    return (
        <div className="battle-setup-container">
            <button className="back-button" onClick={() => navigate("/gym-battle")}>
                ← Back to Trainers
            </button>

            <h1>Battle Setup</h1>
            <p className="setup-subtitle">Select 3 Pokemon to battle against {trainer.name}</p>


            <div className="setup-content">
                {/* Trainer Section - LEFT SIDE */}
                <div className="trainer-section">
                    {getTrainerImage(trainer.name) && (
                        <img
                            src={getTrainerImage(trainer.name)}
                            alt={trainer.name}
                            className="trainer-setup-profile-pic"
                        />
                    )}
                    <h2>Opponent: {trainer.name}</h2>
                    <p className="trainer-type-label">{trainer.type} Type</p>
                    <div className="trainer-team-small">
                        {trainerCards.map((card, index) => (
                            <div key={card.pokedexNumber} className="small-card">
                                <div className="order-badge trainer">{index + 1}</div>
                                <img src={card.imageUrl} alt={card.name} />
                            </div>
                        ))}
                    </div>
                </div>

                {/* Player Selection Section - RIGHT SIDE */}
                <div className="player-section">
                    <h2>Your Team ({selectedPokemon.length}/3 selected)</h2>
                    <p className="selection-hint">Click to select in battle order (1st, 2nd, 3rd)</p>
                    <div className="inventory-grid">
                        {inventory.map((card) => {
                            const isSelected = selectedPokemon.includes(card.pokedexNumber);
                            const orderNumber = getOrderNumber(card.pokedexNumber);
                            return (
                                <div
                                    key={card.pokedexNumber}
                                    className={`selectable-card ${isSelected ? "selected" : ""}`}
                                    onClick={() => handleSelectPokemon(card.pokedexNumber)}
                                >
                                    <img src={card.imageUrl} alt={card.name} />
                                    <p className="card-name">{card.name}</p>
                                    <p className="card-hp">HP: {card.hp}</p>
                                    {isSelected && orderNumber && (
                                        <div className="selected-badge order-badge">{orderNumber}</div>
                                    )}
                                </div>
                            );
                        })}
                    </div>
                </div>
            </div>

            {/* Battle Matchup Preview - Always Visible */}
            <div className="matchup-preview">
                <h3>Battle Order Preview</h3>
                <div className="matchup-grid">
                    {[0, 1, 2].map((index) => {
                        const selectedCards = getSelectedCards();
                        const playerCard = selectedCards[index];
                        const trainerCard = trainerCards[index];

                        return (
                            <div key={index} className="matchup-row">
                                {/* Trainer on LEFT */}
                                <div className="matchup-card trainer-card">
                                    <div className="order-badge trainer">{index + 1}</div>
                                    <img src={trainerCard.imageUrl} alt={trainerCard.name} />
                                    <p className="matchup-name">{trainerCard.name}</p>
                                </div>
                                <div className="vs-indicator">VS</div>
                                {/* Player on RIGHT */}
                                <div className={`matchup-card player-card ${!playerCard ? 'empty' : ''}`}>
                                    <div className="order-badge player">{index + 1}</div>
                                    {playerCard ? (
                                        <>
                                            <img src={playerCard.imageUrl} alt={playerCard.name} />
                                            <p className="matchup-name">{playerCard.name}</p>
                                        </>
                                    ) : (
                                        <div className="empty-slot">
                                            <p className="empty-text">Select Pokemon #{index + 1}</p>
                                        </div>
                                    )}
                                </div>
                            </div>
                        );
                    })}
                </div>
            </div>

            <div className="start-battle-section">
                <button
                    className={`start-battle-button ${selectedPokemon.length === 3 ? "" : "disabled"}`}
                    onClick={handleStartBattle}
                    disabled={selectedPokemon.length !== 3}
                >
                    {selectedPokemon.length === 3 ? "Start Battle!" : `Select ${3 - selectedPokemon.length} more Pokemon`}
                </button>
            </div>
        </div>
    );
};

export default BattleSetup;
