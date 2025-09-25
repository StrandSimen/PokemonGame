package simen.order.loadDB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories

@SpringBootApplication
public class LoadDBApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoadDBApplication.class, args);
    }

}
