package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.dto.CommentOutDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommentMapperTest {
    @Test
    public void testToCommentDto() {
        Comment comment = new Comment(1L, "text", new Item(), new User(), LocalDateTime.now());
        CommentOutDto commentOutDto = CommentMapper.toCommentDto(comment);
        assertNotNull(commentOutDto);
        assertEquals(comment.getId(), commentOutDto.getId());
        assertEquals(comment.getText(), commentOutDto.getText());
        assertEquals(comment.getAuthor().getName(), commentOutDto.getAuthorName());
        assertEquals(comment.getCreated(), commentOutDto.getCreated());
    }

    @Test
    public void testToCommentDtoList() {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment(1L, "text1", new Item(), new User(), LocalDateTime.now()));
        comments.add(new Comment(2L, "text2", new Item(), new User(), LocalDateTime.now()));
        List<CommentOutDto> commentOutDtos = CommentMapper.toCommentDto(comments);
        assertNotNull(commentOutDtos);
        assertEquals(comments.size(), commentOutDtos.size());
    }
}

