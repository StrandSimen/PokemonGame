import React, { useEffect, useState } from "react";
import "./BadgeDisplay.css";
import { API_ENDPOINTS } from "../../config/apiConfig";

interface BadgeProgress {
    earnedBadges: string[];
    totalBadges: number;
    earnedCount: number;
    allBadges: string[];
}

interface BadgeDisplayProps {
    variant?: "compact" | "full";
}

const BADGE_EMOJIS: Record<string, string> = {
    "Volcano Badge": "🌋",
    "Cascade Badge": "💧",
    "Rainbow Badge": "🌈",
};

const BadgeDisplay: React.FC<BadgeDisplayProps> = ({ variant = "compact" }) => {
    const [badgeProgress, setBadgeProgress] = useState<BadgeProgress | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchBadges = async () => {
            try {
                const res = await fetch(API_ENDPOINTS.GYM_BADGE_PROGRESS("defaultUser"));
                if (!res.ok) throw new Error("Failed to fetch badges");
                const data: BadgeProgress = await res.json();
                setBadgeProgress(data);
            } catch (err) {
                console.error("Failed to fetch badges:", err);
            } finally {
                setLoading(false);
            }
        };

        fetchBadges();
    }, []);

    if (loading) return null;
    if (!badgeProgress) return null;

    if (variant === "compact") {
        return (
            <div className="badge-display-compact">
                <div className="badge-counter">
                    Badges: {badgeProgress.earnedCount}/{badgeProgress.totalBadges}
                </div>
                <div className="badge-list-compact">
                    {badgeProgress.allBadges.map((badge) => {
                        const earned = badgeProgress.earnedBadges.includes(badge);
                        return (
                            <span
                                key={badge}
                                className={`badge-icon-compact ${earned ? "earned" : "locked"}`}
                                title={badge}
                            >
                                {BADGE_EMOJIS[badge] || "🏅"}
                            </span>
                        );
                    })}
                </div>
            </div>
        );
    }

    // Full variant for Inventory
    return (
        <div className="badge-display-full">
            <h2 className="badge-section-title">
                Gym Badges ({badgeProgress.earnedCount}/{badgeProgress.totalBadges})
            </h2>
            <div className="badge-grid">
                {badgeProgress.allBadges.map((badge) => {
                    const earned = badgeProgress.earnedBadges.includes(badge);
                    return (
                        <div key={badge} className={`badge-card ${earned ? "earned" : "locked"}`}>
                            <div className="badge-icon-full">
                                {BADGE_EMOJIS[badge] || "🏅"}
                            </div>
                            <div className="badge-name">{badge}</div>
                            {!earned && <div className="badge-status">Not Earned</div>}
                            {earned && <div className="badge-status earned">✓ Earned!</div>}
                        </div>
                    );
                })}
            </div>
        </div>
    );
};

export default BadgeDisplay;

