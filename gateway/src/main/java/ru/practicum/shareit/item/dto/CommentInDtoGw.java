package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentInDtoGw {
    private String text;

    @JsonCreator
    public CommentInDtoGw(@JsonProperty("text") String text) {
        this.text = text;
    }
}
