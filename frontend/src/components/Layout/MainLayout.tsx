import React from "react";
import BoosterPack from "../BoosterPack/BoosterPack.tsx";
import UserPanel from "../UserPanel/UserPanel.tsx";
import "./MainLayout.css";


const MainLayout: React.FC = () => {
    return (
        <div className="main-layout">
            <div className="left-panel">
                <BoosterPack/>
            </div>
            <div className="right-panel">
                <UserPanel/>
            </div>
        </div>
    );
};

export default MainLayout;