import React, { useEffect, useState } from "react";
import type { Card } from "../../types/Card";
import { useNavigate } from "react-router-dom";
import "./Inventory.css"

const Inventory: React.FC = () => {
    const [inventory, setInventory] = useState<Card[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const navigate = useNavigate();

    useEffect(() => {
        const fetchInventory = async () => {
            try {
                setLoading(true);
                setError(null);

                const invRes = await fetch("http://localhost:8100/api/defaultUser/inventory");
                if (!invRes.ok) throw new Error("Failed to fetch inventory");
                const invData: Record<string, number> = await invRes.json();

                const pokedexNumbers = Object.keys(invData);

                const cards: Card[] = await Promise.all(
                    pokedexNumbers.map(async (id) => {
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

        fetchInventory();
    }, []);

    const handleSellCard = async (cardId: number) => {
        try {
            const res = await fetch(`http://localhost:8100/api/defaultUser/sell/${cardId}?sellPrice=20`, {
                method: "POST",
            });
            if (!res.ok) throw new Error("Failed to sell card");

            console.log(`Sold card ${cardId} for 20 coins`);

            setInventory((prev) => {
                const index = prev.findIndex((c) => c.pokedexNumber === cardId);
                if (index === -1) return prev;
                const newInv = [...prev];
                newInv.splice(index, 1);
                return newInv;
            });
        } catch (err) {
            console.error(err);
        }
    };

    if (loading) return <p>Loading inventory...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <div>
            <button onClick={() => navigate("/")} className="open-button">Back</button>

            <div>
                <h2>Inventory</h2>
                <div className="card-inventory-grid">
                    {inventory.map((card) => (
                        <div key={card.pokedexNumber} className="inventory-cards">
                            <img src={card.imageUrl} alt={card.name}/>
                            <button
                                className="sell-button"
                                onClick={() => handleSellCard(card.pokedexNumber)}
                            >
                                Sell for 20 coins
                            </button>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default Inventory;
