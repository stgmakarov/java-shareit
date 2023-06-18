package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Test
    public void getAllBookingTest() throws Exception {
        long userId = 1L;
        String status = "ALL";
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "0")
                        .param("size", "20")
                        .param("state", status))
                .andExpect(status().isOk())
                .andExpect(content().json("[]")
                );
        verify(bookingService, times(1))
                .getAllBooking(userId, status, false, 0L, 20L);
    }

    @Test
    public void getAllBookingOwnerTest() throws Exception {
        long userId = 1L;
        String status = "ALL";
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "0")
                        .param("size", "20")
                        .param("state", status))
                .andExpect(status().isOk())
                .andExpect(content().json("[]")
                );
        verify(bookingService, times(1))
                .getAllBooking(userId, status, true, 0L, 20L);
    }

    @Test
    public void getBookingByIdTest() throws Exception {
        long userId = 1L;
        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk()
                );
        verify(bookingService, times(1))
                .getBooking(1L, userId);
    }

    @Test
    public void approveTest() throws Exception {
        long userId = 1L;
        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "true")
                )
                .andExpect(status().isOk()
                );
        verify(bookingService, times(1)).
                setApprove(1, userId, true);
    }

    @Test
    public void createTest() throws Exception {
        long userId = 1L;
        long itemId = 2L;
        BookingInDto bookingInDto = new BookingInDto(LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                itemId);

        String cont = jacksonObjectMapper.writeValueAsString(bookingInDto);

        mockMvc.perform(post("/bookings")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(cont)));
        verify(bookingService, times(1)).
                create(bookingInDto, userId);
    }
}
