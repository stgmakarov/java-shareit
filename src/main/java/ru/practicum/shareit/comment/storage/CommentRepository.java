package ru.practicum.shareit.comment.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.comment.model.Comment;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c " + "join fetch c.item " + "where c.item.id in ?1")
    List<Comment> findByItem_IdIn(Collection<Long> ids);

    List<Comment> findByItem_id(long itemId);
}
