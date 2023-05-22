package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

/**
 * @author Stanislav Makarov
 */
@UtilityClass
public class UserMapper {
    public static User toUser(UserDto userDto) {
        return new User(0,
                userDto.getName(),
                userDto.getEmail());
    }
}
