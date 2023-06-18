package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserInDto;
import ru.practicum.shareit.user.dto.UserOutDto;
import ru.practicum.shareit.user.model.User;

/**
 * @author Stanislav Makarov
 */
@UtilityClass
public class UserMapper {
    public static User toUser(UserInDto userInDto) {
        return new User(0,
                userInDto.getName(),
                userInDto.getEmail());
    }

    public static UserOutDto toUserOutDto(User user) {
        if (user == null) return null;
        return new UserOutDto(user.getId(),
                user.getName(),
                user.getEmail());
    }
}
