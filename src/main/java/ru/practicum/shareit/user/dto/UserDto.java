package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserDto {
    /*
        id — уникальный идентификатор пользователя;
        name — имя или логин пользователя;
        email — адрес электронной почты (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты).
    */
    private String name;
    @Email
    private String email;
}
