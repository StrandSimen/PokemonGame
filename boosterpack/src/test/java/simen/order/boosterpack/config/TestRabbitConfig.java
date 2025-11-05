package simen.order.boosterpack.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@TestConfiguration
@Profile("test")
public class TestRabbitConfig {

    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate() {
        return mock(RabbitTemplate.class);
    }
}

