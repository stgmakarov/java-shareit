package ru.practicum.shareit.comment;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.storage.CommentDbStorage;
import ru.practicum.shareit.comment.storage.CommentRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommentDbStorageTest {
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentDbStorage commentDbStorage;

    @Test
    public void testCreate() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("text");
        comment.setItem(new Item());

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = commentDbStorage.save(comment);

        assertEquals(result.getId(), comment.getId());
        assertEquals(result.getText(), comment.getText());
    }

    @Test
    public void getCommentByItemTest() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("text");
        comment.setItem(new Item());

        when(commentRepository.findByItem_id(anyLong())).thenReturn(Collections.singletonList(comment));
        List<Comment> commentList = commentDbStorage.getCommentByItem(1L);
        Assertions.assertNotNull(commentList);
        Assertions.assertEquals(commentList.get(0).getId(), comment.getId());
    }

    @Test
    public void getAllCommentsByItemListTest() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("text");
        comment.setItem(new Item());

        when(commentRepository.findByItem_IdIn(anyCollection())).thenReturn(Collections.singletonList(comment));
        List<Comment> commentList = commentDbStorage.getAllCommentsByItemList(Collections.singletonList(new Item()));
        Assertions.assertNotNull(commentList);
        Assertions.assertEquals(commentList.get(0).getId(), comment.getId());
    }
}

