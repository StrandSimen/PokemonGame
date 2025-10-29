package simen.order.autoplayers.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cards")
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
}

