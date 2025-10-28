import React from "react";
import MainLayout from "./components/Layout/MainLayout.tsx";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Inventory from "./components/Inventory/Inventory.tsx";
import TrainerSelection from "./components/GymBattle/TrainerSelection.tsx";
import BattleSetup from "./components/GymBattle/BattleSetup.tsx";
import BattleArena from "./components/GymBattle/BattleArena.tsx";

const App: React.FC = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<MainLayout />} />
                <Route path="/inventory" element={<Inventory />} />
                <Route path="/gym-battle" element={<TrainerSelection />} />
                <Route path="/gym-battle/setup/:trainerName" element={<BattleSetup />} />
                <Route path="/gym-battle/arena/:trainerName" element={<BattleArena />} />
            </Routes>
        </Router>
    );
};

export default App;