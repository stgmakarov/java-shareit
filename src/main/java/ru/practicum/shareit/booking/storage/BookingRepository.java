package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b " +
            "join fetch b.item " +
            "where b.item.id in ?1")
    List<Booking> findByItem_IdIn(Collection<Long> ids);

    List<Booking> findByBookerId(long bookerId);

    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.item")
    List<Booking> findAllWithItem();

    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.item " +
            "WHERE b.item.id = :itemId " +
            "ORDER BY b.start")
    List<Booking> findByItem(@Param("itemId") long itemId);

    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.booker " +
            "WHERE b.start <= :currentDate and " +
            "b.end >= :currentDate and " +
            "b.booker.id = :userId")
    List<Booking> findByDateCurrentBooker(@Param("currentDate") LocalDateTime currentDate,
                                          @Param("userId") long userId);

    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.item " +
            "WHERE b.start <= :currentDate and " +
            "b.end >= :currentDate and " +
            "b.item.owner.id = :userId")
    List<Booking> findByDateCurrentOwner(@Param("currentDate") LocalDateTime currentDate,
                                         @Param("userId") long userId);

    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.booker " +
            "where b.start > :currentDate and " +
            "b.booker.id = :userId")
    List<Booking> findByDateFutureBooker(@Param("currentDate") LocalDateTime currentDate,
                                         @Param("userId") long userId);

    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.item " +
            "where b.start > :currentDate and " +
            "b.item.owner.id = :userId")
    List<Booking> findByDateFutureOwner(@Param("currentDate") LocalDateTime currentDate,
                                        @Param("userId") long userId);


    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.booker " +
            "where b.end < :currentDate and " +
            "b.booker.id = :userId")
    List<Booking> findByDatePastBooker(@Param("currentDate") LocalDateTime currentDate,
                                       @Param("userId") long userId);

    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.item " +
            "where b.end < :currentDate and " +
            "b.item.owner.id = :userId")
    List<Booking> findByDatePastOwner(@Param("currentDate") LocalDateTime currentDate,
                                      @Param("userId") long userId);

    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.booker " +
            "where b.status = :status and " +
            "b.booker.id = :userId")
    List<Booking> findByStatusBooker(@Param("status") BookingStatus bookingStatus,
                                     @Param("userId") long userId);

    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.item " +
            "where b.status = :status and " +
            "b.item.owner.id = :userId")
    List<Booking> findByStatusOwner(@Param("status") BookingStatus bookingStatus,
                                    @Param("userId") long userId);

}
