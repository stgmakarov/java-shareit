package ru.practicum.shareit.comment.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

@JsonTest
public class CommentInDtoTest {
    @Autowired
    private JacksonTester<CommentInDto> json;

    @Test
    public void testDtoFields() throws IOException {
        CommentInDto comment = new CommentInDto("text");

        JsonContent<CommentInDto> dtoJsonContent = json.write(comment);
        Assertions.assertThat(dtoJsonContent).doesNotHaveJsonPath("$.id");
        Assertions.assertThat(dtoJsonContent).hasJsonPath("$.text");
        Assertions.assertThat(dtoJsonContent).doesNotHaveJsonPath("$.authorName");
        Assertions.assertThat(dtoJsonContent).doesNotHaveJsonPath("$.created");
    }
}
