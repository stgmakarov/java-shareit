package ru.practicum.shareit.comment.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentDbStorage implements CommentStorage {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentByItem(long itemId) {
        return commentRepository.findByItem_id(itemId);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getAllCommentsByItemList(List<Item> items) {
        return commentRepository.findByItem_IdIn(items.stream()
                .map(Item::getId)
                .collect(Collectors.toList()));
    }
}
