package simen.order.autoplayers;

import org.springframework.boot.SpringApplication;

public class TestAutoplayersApplication {

    public static void main(String[] args) {
        SpringApplication.from(AutoplayersApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
