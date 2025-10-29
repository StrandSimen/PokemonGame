import React from "react";
import BoosterPack from "../BoosterPack/BoosterPack.tsx";
import UserPanel from "../UserPanel/UserPanel.tsx";
import { useNavigate } from "react-router-dom";
import "./MainLayout.css";


const MainLayout: React.FC = () => {
    const navigate = useNavigate();

    return (
        <div className="main-layout">
            <div className="left-panel">
                <BoosterPack/>
            </div>
            <div className="center-panel">
                <button className="battle-button" onClick={() => navigate("/gym-battle")}>
                    ⚔️ Gym Battle ⚔️
                </button>
            </div>
            <div className="right-panel">
                <UserPanel/>
            </div>
        </div>
    );
};

export default MainLayout;