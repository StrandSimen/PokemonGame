package simen.order.autoplayers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AutoplayersApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoplayersApplication.class, args);
    }

}
