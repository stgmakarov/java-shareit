package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.utilites.ShareitHelper;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ItemRequestService {
    @Autowired
    private ItemRequestRepository requestRepository;
    @Autowired
    private UserStorage userStorage;

    public ItemRequest create(ItemRequestInDto itemRequestInDto, long userId) {
        User user = userStorage.get(userId);

        if (itemRequestInDto.getDescription().isBlank())
            ShareitHelper.returnErrorMsg(HttpStatus.BAD_REQUEST, "Текст не может быть пустой");
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestInDto, user, new ArrayList<>());
        return requestRepository.save(itemRequest);
    }

    public List<ItemRequest> getByUser(long userId) {
        User user = userStorage.get(userId);
        return requestRepository
                .findByRequestor_IdOrderByCreatedAsc(userId);
    }

    public ItemRequest getById(long requestId, long userId) {
        userStorage.get(userId);
        return requestRepository.findById(requestId).orElseThrow(() -> {
            ShareitHelper.returnErrorMsg(HttpStatus.NOT_FOUND, "ID запроса не существует");
            return null;
        });
    }

    public List<ItemRequest> getAll(long userId, Long from, Long size) {
        userStorage.get(userId);
        Pageable pageable = ShareitHelper.getPage(size, from);
        return requestRepository.findByOrderByCreatedAsc(userId, pageable);
    }
}
