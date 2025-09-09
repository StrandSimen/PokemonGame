package simen.order.pokemongame;

import org.springframework.boot.SpringApplication;

public class TestPokemonGameApplication {

    public static void main(String[] args) {
        SpringApplication.from(PokemonGameApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
