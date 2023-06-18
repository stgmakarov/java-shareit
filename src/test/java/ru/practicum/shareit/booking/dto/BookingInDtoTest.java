package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingInDtoTest {
    /*private LocalDateTime start;
    @Future
    @NonNull
    private LocalDateTime end;
    @NonNull
    private Long itemId;*/

    @Autowired
    private JacksonTester<BookingInDto> json;

    @Test
    public void testDtoFields() throws IOException {
        BookingInDto dto = new BookingInDto(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                1L);

        JsonContent<BookingInDto> dtoJsonContent = json.write(dto);

        assertThat(dtoJsonContent).doesNotHaveJsonPath("$.id");
        assertThat(dtoJsonContent).hasJsonPath("$.start");
        assertThat(dtoJsonContent).hasJsonPath("$.end");
        assertThat(dtoJsonContent).hasJsonPath("$.itemId");
        assertThat(dtoJsonContent).doesNotHaveJsonPath("$.item");
        assertThat(dtoJsonContent).doesNotHaveJsonPath("$.booker");
        assertThat(dtoJsonContent).doesNotHaveJsonPath("$.status");
    }
}
