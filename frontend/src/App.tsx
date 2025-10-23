import React from "react";
import MainLayout from "./components/Layout/MainLayout.tsx";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Inventory from "./components/Inventory/Inventory.tsx";

const App: React.FC = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<MainLayout />} />
                <Route path="/inventory" element={<Inventory />} />
            </Routes>
        </Router>
    );
};

export default App;