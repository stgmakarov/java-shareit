package ru.practicum.shareit.item.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

@JsonTest
public class ItemInDtoTest {
    @Autowired
    private JacksonTester<ItemInDto> json;

    @Test
    public void testDtoFields() throws IOException {
        ItemInDto item = new ItemInDto("name", "description", true, 2L);

        JsonContent<ItemInDto> dtoJsonContent = json.write(item);
        Assertions.assertThat(dtoJsonContent).doesNotHaveJsonPath("$.id");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.name");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.description");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.available");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.requestId");
    }
}
