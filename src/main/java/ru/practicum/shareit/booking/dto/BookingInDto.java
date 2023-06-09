package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
public class BookingInDto {
    @Future
    @NonNull
    private LocalDateTime start;
    @Future
    @NonNull
    private LocalDateTime end;
    @NonNull
    private Long itemId;
}
