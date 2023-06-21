package ru.practicum.shareit.item.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;

@JsonTest
public class ItemOutDtoTest {
    @Autowired
    private JacksonTester<ItemOutDto> json;

    @Test
    public void testDtoFields() throws IOException {
        User user = new User();
        user.setName("Name");
        user.setEmail("email@test.com");

        ItemOutDto item = new ItemOutDto(1L, "name", "description", true, user, 2L);

        JsonContent<ItemOutDto> dtoJsonContent = json.write(item);
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.id");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.name");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.description");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.available");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.owner");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.requestId");
    }
}
