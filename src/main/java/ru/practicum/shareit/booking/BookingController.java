package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.utilites.ParamNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    BookingOutDto create(@RequestBody BookingInDto bookingInDto,
                         @RequestHeader("X-Sharer-User-Id") long userId) {
        return BookingMapper.toBookingOutDto(bookingService.create(bookingInDto, userId));
    }

    @PatchMapping("{bookingId}")
    BookingOutDto approv(@RequestHeader("X-Sharer-User-Id") long userId,
                         @PathVariable long bookingId,
                         @RequestParam("approved") boolean approved) {
        return BookingMapper.toBookingOutDto(bookingService.setApprove(bookingId, userId, approved));
    }

    @GetMapping("{bookingId}")
    BookingOutDto getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                             @PathVariable long bookingId) {
        return BookingMapper.toBookingOutDto(bookingService.getBooking(bookingId, userId));
    }

    @GetMapping
    List<BookingOutDto> getAllBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestParam(value = "state", defaultValue = "ALL") String status,
                                      @RequestParam(value = "from", required = false) Long from,
                                      @RequestParam(value = "size", required = false) Long size)
            throws ParamNotFoundException {

        return bookingService.getAllBooking(userId, status, false, from, size).stream()
                .map(BookingMapper::toBookingOutDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    List<BookingOutDto> getAllBookingOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestParam(value = "state", defaultValue = "ALL") String status,
                                           @RequestParam(value = "from", required = false) Long from,
                                           @RequestParam(value = "size", required = false) Long size)
            throws ParamNotFoundException {
        return bookingService.getAllBooking(userId, status, true, from, size).stream()
                .map(BookingMapper::toBookingOutDto)
                .collect(Collectors.toList());
    }
}
