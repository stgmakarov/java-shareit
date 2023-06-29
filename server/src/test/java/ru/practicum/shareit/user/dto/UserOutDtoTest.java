package ru.practicum.shareit.user.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

@JsonTest
public class UserOutDtoTest {
    @Autowired
    private JacksonTester<UserOutDto> json;

    @Test
    public void testDtoFields() throws IOException {
        UserOutDto user = new UserOutDto(1L, "Name", "email@test.com");

        JsonContent<UserOutDto> dtoJsonContent = json.write(user);
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.id");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.name");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.email");
    }
}
