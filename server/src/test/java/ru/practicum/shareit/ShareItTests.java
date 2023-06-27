package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShareItTests {

    @Test
    void testMainApp() {
        Assertions.assertDoesNotThrow(ShareItServer::new);
        Assertions.assertDoesNotThrow(() -> ShareItServer.main(new String[]{}));
    }
}
