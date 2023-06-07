package ru.practicum.shareit.comment;

import ru.practicum.shareit.comment.dto.CommentOutDto;
import ru.practicum.shareit.comment.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentOutDto toCommentDto(Comment comment) {
        return new CommentOutDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static List<CommentOutDto> toCommentDto(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
