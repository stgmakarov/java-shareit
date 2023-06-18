package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserInDto;
import ru.practicum.shareit.user.storage.UserStorage;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockBean
    private UserStorage userStorage;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Test
    public void createTest() throws Exception {
        UserInDto userInDto = new UserInDto("имя фамилия", "email@mail.com");
        String cont = jacksonObjectMapper.writeValueAsString(userInDto);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(cont)));
        verify(userStorage, times(1))
                .create(userInDto);
    }

    @Test
    public void updateTest() throws Exception {
        long userId = 1L;
        UserInDto userInDto = new UserInDto("имя фамилия", "email@mail.com");
        String cont = jacksonObjectMapper.writeValueAsString(userInDto);

        mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(cont))
        );
        verify(userStorage, times(1))
                .update(userId, userInDto);
    }

    @Test
    public void getAllUsersTest() throws Exception {

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
        verify(userStorage, times(1))
                .getAll();
    }

    @Test
    public void getUserTest() throws Exception {
        long userId = 1L;

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
        verify(userStorage, times(1))
                .get(userId);
    }

    @Test
    public void delUserTest() throws Exception {
        long userId = 1L;

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
        verify(userStorage, times(1))
                .delete(userId);
    }
}
