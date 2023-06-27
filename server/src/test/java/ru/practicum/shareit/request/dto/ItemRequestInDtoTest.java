package ru.practicum.shareit.request.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

@JsonTest
public class ItemRequestInDtoTest {
    @Autowired
    private JacksonTester<ItemRequestInDto> json;

    @Test
    public void testDtoFields() throws IOException {
        ItemRequestInDto itemRequest = new ItemRequestInDto("description");

        JsonContent<ItemRequestInDto> dtoJsonContent = json.write(itemRequest);
        Assertions.assertThat(dtoJsonContent).doesNotHaveJsonPath("$.id");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.description");
        Assertions.assertThat(dtoJsonContent).doesNotHaveJsonPath("$.requestor");
        Assertions.assertThat(dtoJsonContent).doesNotHaveJsonPath("$.created");
        Assertions.assertThat(dtoJsonContent).doesNotHaveJsonPath("$.items");
    }
}
