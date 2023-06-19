package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ItemRequestServiceTest {
    UserDbStorage userStorage;
    ItemRequestRepository requestRepository;
    ItemRequestService itemRequestService;

    User user;

    @BeforeEach
    public void init() {
        userStorage = Mockito.mock(UserDbStorage.class);
        requestRepository = Mockito.mock(ItemRequestRepository.class);

        itemRequestService = new ItemRequestService(requestRepository,
                userStorage);

        user = new User(1L, "User name", "user@mail.com");
    }

    @Test
    public void createItemRequestTest() {
        when(userStorage.get(anyLong())).thenReturn(user);
        when(requestRepository.save(any(ItemRequest.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));
        String desc = "description";
        ItemRequest result = itemRequestService.create(
                new ItemRequestInDto(desc),
                user.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getDescription(), desc);
    }

    @Test
    public void createItemRequestFail1Test() {
        when(userStorage.get(anyLong())).thenReturn(user);
        when(requestRepository.save(any(ItemRequest.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));
        String desc = "";

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            itemRequestService.create(
                    new ItemRequestInDto(desc),
                    user.getId());
        });
    }

    @Test
    public void getByUserTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(10L);
        when(userStorage.get(anyLong())).thenReturn(user);
        when(requestRepository.findByRequestor_IdOrderByCreatedAsc(
                anyLong())).thenReturn(Collections.singletonList(itemRequest));

        List<ItemRequest> result = itemRequestService.getByUser(user.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.get(0).getId(), itemRequest.getId());
    }

    @Test
    public void getBygetByIdTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(10L);
        when(userStorage.get(anyLong())).thenReturn(user);
        when(requestRepository.findById(
                anyLong())).thenReturn(Optional.of(itemRequest));

        ItemRequest result = itemRequestService.getById(itemRequest.getId(), user.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), itemRequest.getId());
    }

    @Test
    public void getBygetByIdFail1Test() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(10L);
        when(userStorage.get(anyLong())).thenReturn(user);
        when(requestRepository.findById(
                anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            itemRequestService.getById(itemRequest.getId(), user.getId());
        });
    }

    @Test
    public void getAllTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(10L);
        when(userStorage.get(anyLong())).thenReturn(user);
        when(requestRepository.findByOrderByCreatedAsc(
                anyLong(), any(Pageable.class))).thenReturn(Collections.singletonList(itemRequest));

        List<ItemRequest> result = itemRequestService.getAll(user.getId(), 0L, 20L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.get(0).getId(), itemRequest.getId());
    }

}
