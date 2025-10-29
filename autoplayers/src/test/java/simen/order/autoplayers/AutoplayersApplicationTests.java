package simen.order.autoplayers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {
        "spring.cloud.consul.enabled=false",
        "spring.cloud.discovery.enabled=false"
    }
)
@ActiveProfiles("test")
class AutoplayersApplicationTests {

    @Test
    void contextLoads() {
        // Simple smoke test to verify application context loads
    }

}
