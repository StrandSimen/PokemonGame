package simen.order.frontend;

import org.springframework.boot.SpringApplication;

public class TestFrontendApplication {

    public static void main(String[] args) {
        SpringApplication.from(FrontendApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
