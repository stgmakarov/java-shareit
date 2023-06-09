package ru.practicum.shareit.comment.storage;

import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface CommentStorage {
    List<Comment> getCommentByItem(long itemId);

    Comment save(Comment comment);

    List<Comment> getAllCommentsByItemList(List<Item> items);
}
