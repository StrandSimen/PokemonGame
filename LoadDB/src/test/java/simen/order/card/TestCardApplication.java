package simen.order.card;

import org.springframework.boot.SpringApplication;

public class TestCardApplication {

    public static void main(String[] args) {
        SpringApplication.from(LoadDBApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
