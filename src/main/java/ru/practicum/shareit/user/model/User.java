package ru.practicum.shareit.user.model;

import lombok.Data;

@Data
public class User {
    /*
        id — уникальный идентификатор пользователя;
        name — имя или логин пользователя;
        email — адрес электронной почты (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты).
    */
    private long id;
    private String name;
    private String email;

    public User(long id, String name, String email) {
        this.id = id;
        if (name != null) this.name = name;
        else this.name = "";
        if (email != null) this.email = email;
        else this.email = "";
    }
}
