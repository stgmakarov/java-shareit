package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentInDto {
    private String text;

    @JsonCreator
    public CommentInDto(@JsonProperty("text") String text) {
        this.text = text;
    }
}
