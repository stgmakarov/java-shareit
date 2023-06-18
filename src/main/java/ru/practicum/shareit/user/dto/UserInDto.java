package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInDto {
    /*
        id — уникальный идентификатор пользователя;
        name — имя или логин пользователя;
        email — адрес электронной почты (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты).
    */
    private String name;
    @Email
    private String email;
}
