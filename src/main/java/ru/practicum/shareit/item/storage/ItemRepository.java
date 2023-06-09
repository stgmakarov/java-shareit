package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    //@Query()
    List<Item> findAllByOwner_IdOrderById(long owner);

    List<Item> findByDescriptionLikeIgnoreCaseAndAvailableEquals(String text, Boolean available);

    boolean existsByIdAndAvailable(long id, Boolean available);
}
