package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Entity
@Table(name = "items", schema = "public")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Item {
    /*    id — уникальный идентификатор вещи;
        name — краткое название;
        description — развёрнутое описание;
        available — статус о том, доступна или нет вещь для аренды;
        owner — владелец вещи;
        request — если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    @Column(name = "is_available")
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}
