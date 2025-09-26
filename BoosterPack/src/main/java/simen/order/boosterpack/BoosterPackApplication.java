package simen.order.boosterpack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "simen.order.card.repository")
@EntityScan(basePackages = "simen.order.card.model")
public class BoosterPackApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoosterPackApplication.class, args);
    }

}
