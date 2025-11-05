package simen.order.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import simen.order.user.config.TestRabbitConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Import(TestRabbitConfig.class)
class UserApplicationTests {

    @Test
    void contextLoads() {
    }

}