import React, {useEffect, useState} from "react";
import type {Card} from "../../types/Card.ts";
import "./UserPanel.css";
import ashKetchum from "../pictures/ashKetchum.png"


const UserPanel: React.FC = () => {
    const[coins, setCoins] = useState<number>(0);
    const[inventory, setInventory] = useState<Card[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const fetchUserData = async () => {
        try {
            setLoading(true);
            setError(null);

            // Fetch coins
            const coinsRes = await fetch("http://localhost:8081/api/defaultUser/coins");
            if (!coinsRes.ok) throw new Error("Failed to fetch user coins");
            const coinsData = await coinsRes.json();
            setCoins(Number(coinsData)); // just use the number directly


            // Fetch inventory
            const invRes = await fetch("http://localhost:8081/api/defaultUser/inventory");
            console.log(invRes);
            if (!invRes.ok) throw new Error("Failed to fetch inventory");
            const invData: Record<string, number> = await invRes.json();

            const pokedexNumbers = Object.keys(invData);

            const card: Card[] = await Promise.all(
                pokedexNumbers.map(async (id) => {
                    const res = await fetch(`http://localhost:8080/api/cards/${id}`);
                    if (!res.ok) throw new Error(`Failed to fetch card ${id}`);
                    return res.json() as Promise<Card>;
                })
            );

            setInventory(card);

        } catch (err) {
            setError((err as Error).message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUserData();
    }, []);

    if (loading) return <p>Loading user data...</p>;
    if (error) return <p>Error: {error}</p>;

    return(
        <div className="user-panel">
            <h2>Ash Ketchum</h2>
            <img src={ashKetchum} alt="ashKetchum picture" className="profile-picture"/>
            <p>Coins: {coins}</p>

            <h3>Inventory:</h3>
            {inventory.length > 0 ? (
                <div className="card-grid">
                    {inventory.slice(0, 6).map((card) => (
                        <div key={card.pokedexNumber} className="user-card">
                            <img src={card.imageUrl} alt={card.name}/>
                        </div>
                    ))}
                </div>
            ) : (
                <p>No cards in inventory yet.</p>
            )}

            <button className="open-button" onClick={fetchUserData}>
                Refresh
            </button>
        </div>
    )
};

export default UserPanel