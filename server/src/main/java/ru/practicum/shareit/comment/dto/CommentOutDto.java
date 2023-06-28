package ru.practicum.shareit.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentOutDto {
    /*    id — уникальный идентификатор комментария;
    text — содержимое комментария;
    author — автор комментария;
    created — дата создания комментария.*/
    private long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
