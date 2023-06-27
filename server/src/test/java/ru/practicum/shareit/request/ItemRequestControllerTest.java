package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Test
    public void createTest() throws Exception {
        long userId = 1L;
        ItemRequestInDto itemRequestInDto = new ItemRequestInDto("Всё нужно");

        String cont = jacksonObjectMapper.writeValueAsString(itemRequestInDto);

        mockMvc.perform(post("/requests")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(cont)));
        verify(itemRequestService, times(1))
                .create(itemRequestInDto, userId);
    }

    @Test
    public void getUserRequestsTest() throws Exception {
        long userId = 1L;
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]")
                );
        verify(itemRequestService, times(1))
                .getByUser(userId);
    }

    @Test
    public void getRequestByIdTest() throws Exception {
        long userId = 1L;
        long requestId = 2L;
        mockMvc.perform(get("/requests/2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
        verify(itemRequestService, times(1))
                .getById(requestId, userId);
    }

    @Test
    public void getAllRequestsTest() throws Exception {
        long userId = 1L;
        long from = 0;
        long size = 20L;
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());
        verify(itemRequestService, times(1))
                .getAll(userId, from, size);
    }
}
