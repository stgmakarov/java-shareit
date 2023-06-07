package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings")
@AllArgsConstructor
@RequiredArgsConstructor
public class Booking {
    /*    id — уникальный идентификатор бронирования;
        start — дата и время начала бронирования;
        end — дата и время конца бронирования;
        item — вещь, которую пользователь бронирует;
        booker — пользователь, который осуществляет бронирование;
        status — статус бронирования.
            Может принимать одно из следующих значений:
                WAITING — новое бронирование, ожидает одобрения,
                APPROVED — Дополнительные советы ментора2 бронирование подтверждено владельцем,
                REJECTED — бронирование отклонено владельцем,
                CANCELED— бронирование отменено создателем*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User booker;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

}
