package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingOutDtoTest2 {
/*    private long id;
    private long bookerId;*/

    @Autowired
    private JacksonTester<BookingOutDto2> json;

    @Test
    public void testDtoFields() throws IOException {
        BookingOutDto2 dto = new BookingOutDto2(1L,
                2L);

        JsonContent<BookingOutDto2> dtoJsonContent = json.write(dto);
        assertThat(dtoJsonContent).hasJsonPath("$.id");
        assertThat(dtoJsonContent).hasJsonPath("$.bookerId");
        assertThat(dtoJsonContent).doesNotHaveJsonPath("$.start");
        assertThat(dtoJsonContent).doesNotHaveJsonPath("$.end");
        assertThat(dtoJsonContent).doesNotHaveJsonPath("$.item");
        assertThat(dtoJsonContent).doesNotHaveJsonPath("$.booker");
        assertThat(dtoJsonContent).doesNotHaveJsonPath("$.status");
    }
}
