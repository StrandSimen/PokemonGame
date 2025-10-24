package simen.order.boosterpack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BoosterPackApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoosterPackApplication.class, args);
    }

}
