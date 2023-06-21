package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentInDto;
import ru.practicum.shareit.item.dto.ItemInDto;
import ru.practicum.shareit.item.service.ItemService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Test
    public void createTest() throws Exception {
        long userId = 1L;
        ItemInDto itemInDto = new ItemInDto("Весчь", "Описание", true, 1L);
        String cont = jacksonObjectMapper.writeValueAsString(itemInDto);

        mockMvc.perform(post("/items")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(cont)));
        verify(itemService, times(1))
                .create(itemInDto, userId);
    }

    @Test
    public void updateTest() throws Exception {
        long userId = 1L;
        ItemInDto itemInDto = new ItemInDto("Весчь", "Опис---ание", true, 1L);
        String cont = jacksonObjectMapper.writeValueAsString(itemInDto);

        mockMvc.perform(patch("/items/1")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(cont))
        );
        verify(itemService, times(1))
                .update(1L, itemInDto, userId);
    }

    @Test
    public void getItemByIdTest() throws Exception {
        long userId = 1L;
        long itemId = 2L;

        mockMvc.perform(get("/items/2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
        verify(itemService, times(1))
                .getByIdDto2(itemId, userId);
    }

    @Test
    public void getAllMyItemsTest() throws Exception {
        long userId = 1L;

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemService, times(1))
                .getAllUserItems(userId);
    }

    @Test
    public void getItemCommentTest() throws Exception {
        mockMvc.perform(get("/items/1/comment"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemService, times(1))
                .getItemComment(1L);
    }

    @Test
    public void addCommentTest() throws Exception {
        long userId = 1L;
        long itemId = 2L;
        CommentInDto comment = new CommentInDto("Хорошая весчь");
        String cont = jacksonObjectMapper.writeValueAsString(comment);

        mockMvc.perform(post("/items/2/comment")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(cont)));
        verify(itemService, times(1))
                .addComment(itemId, userId, "Хорошая весчь");
    }
}
