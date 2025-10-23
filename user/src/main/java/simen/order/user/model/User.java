package simen.order.user.model;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "user_username") // Match the existing table name
public class User {
    @Id
    @Column(name = "username") // Match the column name in user_username
    private String username;

    @ElementCollection
    @CollectionTable(name = "inventory", joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"))
    @MapKeyColumn(name = "pokedex_number") // Key column for the map
    @Column(name = "amount") // Value column for the map
    private Map<Integer, Integer> inventory = new HashMap<>(); // Initialize to avoid null

    @Column(name = "coins")
    private Integer coins = 100;

    public int getCoins() {
        return coins;
    }
    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<Integer, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<Integer, Integer> inventory) {
        this.inventory = inventory;
    }
}