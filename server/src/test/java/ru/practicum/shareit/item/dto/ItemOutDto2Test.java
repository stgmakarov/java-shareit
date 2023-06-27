package ru.practicum.shareit.item.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingOutDto2;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.util.ArrayList;

@JsonTest
public class ItemOutDto2Test {
    @Autowired
    private JacksonTester<ItemOutDto2> json;

    @Test
    public void testDtoFields() throws IOException {
        User user = new User();
        user.setName("Name");
        user.setEmail("email@test.com");

        ItemOutDto2 item = new ItemOutDto2(1L, "name",
                "description", true, user,
                2L, new BookingOutDto2(1L, 2L),
                new BookingOutDto2(3L, 4L), new ArrayList<>());

        JsonContent<ItemOutDto2> dtoJsonContent = json.write(item);
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.id");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.name");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.description");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.available");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.owner");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.requestId");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.lastBooking");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.nextBooking");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.comments");
    }
}
