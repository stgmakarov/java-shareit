package ru.practicum.shareit.comment.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.model.Comment;

import java.util.List;

@Component
public class CommentDbStorage {
    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getCommentByItem(long itemId) {
        return commentRepository.findByItem_id(itemId);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
}
