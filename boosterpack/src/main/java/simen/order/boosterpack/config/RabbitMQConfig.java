package simen.order.boosterpack.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue boosterQueue() {
        return new Queue("booster.queue", false); // Non-durable queue
    }
}

