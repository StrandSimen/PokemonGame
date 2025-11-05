package simen.order.boosterpack;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import simen.order.boosterpack.config.TestRabbitConfig;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestRabbitConfig.class)
class BoosterPackApplicationTests {

    @Test
    void contextLoads() {
    }

}
