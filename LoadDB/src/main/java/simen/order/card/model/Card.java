package simen.order.card.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "cards")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {

    @Id
    private Integer pokedexNumber;
    @Column(nullable = false)
    private String name;
    private String hp;
    @Column(length = 100)
    private String types;
    @Column(length = 500)
    private String imageUrl;

    @Transient
    private List<Integer> nationalPokedexNumbers;

    @JsonProperty("nationalPokedexNumbers")
    private void unpackNationalPokedexNumbers(List<Integer> numbers) {
        this.nationalPokedexNumbers = numbers;
    }

    @JsonProperty("types")
    private void unpackTypes(String[] types) {
        if (types != null) {
            this.types = String.join(",", types);
        }
    }

    @JsonProperty("images")
    private void unpackImageUrls(Map<String, String> images) {
        if (images != null) {
            this.imageUrl = images.get("small");
        }
    }

    public Integer getPokedexNumber() { return pokedexNumber; }
    public void setPokedexNumber(Integer pokedexNumber) { this.pokedexNumber = pokedexNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHp() { return hp; }
    public void setHp(String hp) { this.hp = hp; }

    public String getTypes() { return types; }
    public void setTypes(String types) { this.types = types; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<Integer> getNationalPokedexNumbers() { return nationalPokedexNumbers; }
}
