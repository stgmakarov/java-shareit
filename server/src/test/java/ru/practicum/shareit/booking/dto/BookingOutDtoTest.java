package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingOutDtoTest {
    /*    private long id;
        private LocalDateTime start;
        private LocalDateTime end;
        private Item item;
        private User booker;
        private BookingStatus status;*/

    @Autowired
    private JacksonTester<BookingOutDto> json;

    @Test
    public void testDtoFields() throws IOException {
        BookingOutDto dto = new BookingOutDto(1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                new Item(),
                new User(),
                BookingStatus.WAITING);

        JsonContent<BookingOutDto> dtoJsonContent = json.write(dto);
        assertThat(dtoJsonContent).hasJsonPath("$.id");
        assertThat(dtoJsonContent).hasJsonPath("$.start");
        assertThat(dtoJsonContent).hasJsonPath("$.end");
        assertThat(dtoJsonContent).hasJsonPath("$.item");
        assertThat(dtoJsonContent).hasJsonPath("$.booker");
        assertThat(dtoJsonContent).hasJsonPath("$.status");
        assertThat(dtoJsonContent).hasJsonPathValue("$.start");
        assertThat(dtoJsonContent).hasJsonPathValue("$.end");
    }
}
