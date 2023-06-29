package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto2;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    @Autowired
    private ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestOutDto create(@RequestBody ItemRequestInDto itemRequestInDto,
                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        return ItemRequestMapper.toItemRequestOutDto(itemRequestService.create(itemRequestInDto, userId));
    }

    @GetMapping
    public List<ItemRequestOutDto2> getUserRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getByUser(userId).stream()
                .map(ItemRequestMapper::toItemRequestOutDto2)
                .collect(Collectors.toList());
    }

    @GetMapping("/{requestId}")
    public ItemRequestOutDto getRequestById(@PathVariable long requestId,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
        return ItemRequestMapper.toItemRequestOutDto(itemRequestService.getById(requestId, userId));
    }


    @GetMapping("/all")
    public List<ItemRequestOutDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(value = "from", required = false) Long from,
                                                  @RequestParam(value = "size", required = false) Long size) {
        return itemRequestService.getAll(userId, from, size).stream()
                .map(ItemRequestMapper::toItemRequestOutDto)
                .collect(Collectors.toList());
    }


}
