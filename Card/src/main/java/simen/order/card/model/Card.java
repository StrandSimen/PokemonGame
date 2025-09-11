package simen.order.card.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {
    private String name;
    private String hp;
    private String[] types;
    private String imageUrl;

    @JsonProperty("nationalPokedexNumbers")
    private List<Integer> nationalPokedexNumbers;

    @JsonProperty("images")
    private void unpackImageUrls(java.util.Map<String, String> images) {
        if (images != null) {
            this.imageUrl = images.get("small");
        }
    }

    public List<Integer> getNationalPokedexNumbers() { return nationalPokedexNumbers; }
    public void setNationalPokedexNumbers(List<Integer> nationalPokedexNumbers) { this.nationalPokedexNumbers = nationalPokedexNumbers; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHp() { return hp; }
    public void setHp(String hp) { this.hp = hp; }

    public String[] getTypes() { return types; }
    public void setTypes(String[] types) { this.types = types; }

    public String getImages() { return imageUrl; }
    public void setImageUrl() { this.imageUrl = imageUrl; }
}
