package ru.practicum.shareit.comment.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

@JsonTest
public class CommentOutDtoTest {
    @Autowired
    private JacksonTester<CommentOutDto> json;

    @Test
    public void testDtoFields() throws IOException {
        CommentOutDto comment = new CommentOutDto(1L, "text", "authorName", LocalDateTime.now());

        JsonContent<CommentOutDto> dtoJsonContent = json.write(comment);
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.id");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.text");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.authorName");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.created");
    }
}
