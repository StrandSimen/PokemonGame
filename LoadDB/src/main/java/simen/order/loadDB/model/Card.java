package simen.order.loadDB.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.Map;

@Entity  // Mark as JPA entity
@Table(name = "cards")  // Table name in DB
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {

    @Id  // Primary key
    private Integer pokedexNumber;  // Use as ID (assuming unique per species)

    private String name;
    private String hp;
    private String types;  // Store as comma-separated string (e.g., "Grass,Poison")
    private String imageUrl;

    @JsonProperty("nationalPokedexNumbers")
    private void setNationalPokedexNumbers(java.util.List<Integer> nationalPokedexNumbers) {
        if (nationalPokedexNumbers != null && !nationalPokedexNumbers.isEmpty()) {
            this.pokedexNumber = nationalPokedexNumbers.get(0);  // Take first as ID
        }
    }

    @JsonProperty("types")
    private void setTypes(String[] types) {
        if (types != null) {
            this.types = String.join(",", types);  // Convert array to string
        }
    }

    @JsonProperty("images")
    private void unpackImageUrls(Map<String, String> images) {
        if (images != null) {
            this.imageUrl = images.get("small");
        }
    }

    // Getters and Setters
    public Integer getPokedexNumber() { return pokedexNumber; }
    public void setPokedexNumber(Integer pokedexNumber) { this.pokedexNumber = pokedexNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHp() { return hp; }
    public void setHp(String hp) { this.hp = hp; }

    public String getTypes() { return types; }  // Return as string; split by "," if needed
    public void setTypes(String types) { this.types = types; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}