package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto2;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequestOutDto toItemRequestOutDto(ItemRequest itemRequest) {
        if (itemRequest == null) return null;
        return new ItemRequestOutDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor(),
                itemRequest.getCreated(),
                itemRequest.getItems().stream().map(ItemMapper::toItemOutDto).collect(Collectors.toList()));

    }

    public static ItemRequestOutDto2 toItemRequestOutDto2(ItemRequest itemRequest) {
        return new ItemRequestOutDto2(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor(),
                itemRequest.getCreated(),
                itemRequest.getItems().stream().map(ItemMapper::toItemOutDto).collect(Collectors.toList()));

    }

    public static ItemRequest toItemRequest(ItemRequestInDto requestInDto, User user, List<Item> items) {
        return new ItemRequest(0,
                requestInDto.getDescription(),
                user,
                LocalDateTime.now(),
                items);
    }
}
