package ru.practicum.shareit.booking.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
public class BookingInDto {
    @FutureOrPresent
    @NonNull
    private LocalDateTime start;
    @Future
    @NonNull
    private LocalDateTime end;
    @NonNull
    private Long itemId;
}
