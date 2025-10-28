package simen.order.autoplayers.model;

import java.util.List;

public class GymTrainer {
    private String name;
    private String type;
    private List<Integer> pokemonTeam; // List of 3 pokedex numbers

    public GymTrainer(String name, String type, List<Integer> pokemonTeam) {
        this.name = name;
        this.type = type;
        this.pokemonTeam = pokemonTeam;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public List<Integer> getPokemonTeam() { return pokemonTeam; }
    public void setPokemonTeam(List<Integer> pokemonTeam) { this.pokemonTeam = pokemonTeam; }
}

