package ru.practicum.shareit.request.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonTest
public class ItemRequestOutDtoTest {
    @Autowired
    private JacksonTester<ItemRequestOutDto> json;

    @Test
    public void testDtoFields() throws IOException {
        User user = new User();
        user.setName("Name");
        user.setEmail("email@test.com");

        ItemOutDto item = new ItemOutDto(1L, "name", "description", true, user,
                2L);
        List<ItemOutDto> items = new ArrayList<>();
        items.add(item);

        ItemRequestOutDto itemRequest = new ItemRequestOutDto(1L, "description", user,
                LocalDateTime.now(), items);

        JsonContent<ItemRequestOutDto> dtoJsonContent = json.write(itemRequest);
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.id");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.description");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.requestor");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.created");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.items");
    }
}
