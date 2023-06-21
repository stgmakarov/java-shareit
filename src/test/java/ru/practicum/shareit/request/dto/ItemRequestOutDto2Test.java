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
public class ItemRequestOutDto2Test {
    /*    private long id;
        private String description;
        private User requestor;
        private LocalDateTime created;
        private List<ItemOutDto> items;*/
    @Autowired
    private JacksonTester<ItemRequestOutDto2> json;

    @Test
    public void testDtoFields() throws IOException {
        User user = new User();
        user.setName("Name");
        user.setEmail("email@test.com");

        ItemOutDto item = new ItemOutDto(1L, "name", "description", true, user,
                2L);
        List<ItemOutDto> items = new ArrayList<>();
        items.add(item);

        ItemRequestOutDto2 itemRequest = new ItemRequestOutDto2(1L, "description", user,
                LocalDateTime.now(), items);

        JsonContent<ItemRequestOutDto2> dtoJsonContent = json.write(itemRequest);
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.id");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.description");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.requestor");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.created");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.items");
    }
}
