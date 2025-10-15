import type {Card} from "./Card.ts";

export interface User {
    id: string;
    name: string;
    coins: number;
    inventory: Card[];
}