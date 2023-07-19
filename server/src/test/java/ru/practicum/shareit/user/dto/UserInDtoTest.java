package ru.practicum.shareit.user.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

@JsonTest
public class UserInDtoTest {
    @Autowired
    private JacksonTester<UserInDto> json;

    @Test
    public void testDtoFields() throws IOException {
        UserInDto user = new UserInDto();
        user.setName("Name");
        user.setEmail("email@test.com");

        JsonContent<UserInDto> dtoJsonContent = json.write(user);
        Assertions.assertThat(dtoJsonContent).doesNotHaveJsonPath("$.id");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.name");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.email");
    }
}
