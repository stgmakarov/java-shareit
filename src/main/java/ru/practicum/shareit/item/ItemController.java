package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    ItemService itemService;

    @PostMapping
    public Item create(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.create(ItemMapper.toItem(itemDto), userId);
    }

    @PatchMapping("{itemId}")
    public Item update(@PathVariable long itemId,
                       @RequestBody ItemDto itemDto,
                       @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.update(itemId, ItemMapper.toItem(itemDto), userId);
    }

    @GetMapping("{itemId}")
    public Item getItemById(@PathVariable long itemId) {
        return itemService.getById(itemId);
    }

    @GetMapping
    public List<Item> getAllMyItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllUserItems(userId);
    }

    @GetMapping("/search")
    public List<Item> findItems(@RequestParam String text) {
        return itemService.findByText(text);
    }

}
