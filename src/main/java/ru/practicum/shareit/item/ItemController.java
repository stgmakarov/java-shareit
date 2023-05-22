package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        return ItemMapper.toItemDto(itemService.create(ItemMapper.toItem(itemDto), userId));
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@PathVariable long itemId,
                          @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return ItemMapper.toItemDto(itemService.update(itemId, ItemMapper.toItem(itemDto), userId));
    }

    @GetMapping("{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return ItemMapper.toItemDto(itemService.getById(itemId));
    }

    @GetMapping
    public List<ItemDto> getAllMyItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllUserItems(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> findItems(@RequestParam String text) {
        return itemService.findByText(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

}
