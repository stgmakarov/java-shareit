package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserInDto;
import ru.practicum.shareit.user.dto.UserOutDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    @Test
    public void testToUser() {
        UserInDto userInDto = new UserInDto("John", "john@example.com");
        User user = UserMapper.toUser(userInDto);
        assertEquals(0, user.getId());
        assertEquals(userInDto.getName(), user.getName());
        assertEquals(userInDto.getEmail(), user.getEmail());
    }

    @Test
    public void testToUserOutDto() {
        User user = new User(1L, "John", "john@example.com");
        UserOutDto userOutDto = UserMapper.toUserOutDto(user);
        assertNotNull(userOutDto);
        assertEquals(user.getId(), userOutDto.getId());
        assertEquals(user.getName(), userOutDto.getName());
        assertEquals(user.getEmail(), userOutDto.getEmail());
    }

    @Test
    public void testToUserOutDtoWithNullUser() {
        User user = null;
        UserOutDto userOutDto = UserMapper.toUserOutDto(user);
        assertNull(userOutDto);
    }
}

