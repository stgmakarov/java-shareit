package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingOutDto2;
import ru.practicum.shareit.comment.dto.CommentOutDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemOutDto2 {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private Long request;
    private BookingOutDto2 lastBooking;
    private BookingOutDto2 nextBooking;
    private List<CommentOutDto> comments;
}
