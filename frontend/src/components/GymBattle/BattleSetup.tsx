import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import type { Card } from "../../types/Card";
import type { GymTrainer } from "../../types/GymBattle";
import "./BattleSetup.css";

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
                const trainerRes = await fetch(`http://localhost:8100/api/gym/trainers/${trainerName}`);
                if (!trainerRes.ok) throw new Error("Failed to fetch trainer");
                const trainerData: GymTrainer = await trainerRes.json();
                setTrainer(trainerData);

                // Fetch trainer's cards
                const tCards: Card[] = await Promise.all(
                    trainerData.pokemonTeam.map(async (id) => {
                        const res = await fetch(`http://localhost:8100/api/cards/${id}`);
                        if (!res.ok) throw new Error(`Failed to fetch card ${id}`);
                        return res.json() as Promise<Card>;
                    })
                );
                setTrainerCards(tCards);

                // Fetch player inventory
                const invRes = await fetch("http://localhost:8100/api/defaultUser/inventory");
                if (!invRes.ok) throw new Error("Failed to fetch inventory");
                const invData: Record<string, number> = await invRes.json();

                const cards: Card[] = await Promise.all(
                    Object.keys(invData).map(async (id) => {
                        const res = await fetch(`http://localhost:8100/api/cards/${id}`);
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
            setSelectedPokemon(selectedPokemon.filter((id) => id !== pokedexNumber));
        } else if (selectedPokemon.length < 3) {
            setSelectedPokemon([...selectedPokemon, pokedexNumber]);
        }
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
                {/* Trainer Section */}
                <div className="trainer-section">
                    <h2>Opponent: {trainer.name}</h2>
                    <p className="trainer-type-label">{trainer.type} Type</p>
                    <div className="trainer-team-small">
                        {trainerCards.map((card) => (
                            <div key={card.pokedexNumber} className="small-card">
                                <img src={card.imageUrl} alt={card.name} />
                            </div>
                        ))}
                    </div>
                </div>

                {/* Player Selection Section */}
                <div className="player-section">
                    <h2>Your Team ({selectedPokemon.length}/3 selected)</h2>
                    <div className="inventory-grid">
                        {inventory.map((card) => {
                            const isSelected = selectedPokemon.includes(card.pokedexNumber);
                            return (
                                <div
                                    key={card.pokedexNumber}
                                    className={`selectable-card ${isSelected ? "selected" : ""}`}
                                    onClick={() => handleSelectPokemon(card.pokedexNumber)}
                                >
                                    <img src={card.imageUrl} alt={card.name} />
                                    <p className="card-name">{card.name}</p>
                                    <p className="card-hp">HP: {card.hp}</p>
                                    {isSelected && <div className="selected-badge">✓</div>}
                                </div>
                            );
                        })}
                    </div>
                </div>
            </div>

            <div className="start-battle-section">
                <button
                    className={`start-battle-button ${selectedPokemon.length === 3 ? "" : "disabled"}`}
                    onClick={handleStartBattle}
                    disabled={selectedPokemon.length !== 3}
                >
                    {selectedPokemon.length === 3 ? "Start Battle! ⚔️" : `Select ${3 - selectedPokemon.length} more Pokemon`}
                </button>
            </div>
        </div>
    );
};

export default BattleSetup;

