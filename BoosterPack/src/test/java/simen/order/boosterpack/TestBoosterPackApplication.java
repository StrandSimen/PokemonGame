package simen.order.boosterpack;

import org.springframework.boot.SpringApplication;

public class TestBoosterPackApplication {

    public static void main(String[] args) {
        SpringApplication.from(BoosterPackApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
