package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequestor_IdOrderByCreatedAsc(long id);

    @Query("select i from ItemRequest i where i.requestor.id != :requestorId order by i.created")
    List<ItemRequest> findByOrderByCreatedAsc(@Param("requestorId") long requestorId, Pageable pageable);
}
