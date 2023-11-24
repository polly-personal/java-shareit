package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoOut create(long userId, ItemRequestDtoIn itemRequestDtoIn);

    ItemRequestDtoOut getById(Long itemRequestId);

    List<ItemRequestDtoOut> getAllForRequester(long userId);

    List<ItemRequestDtoOut> getAllOtherUsersRequests(long userId, int from, int size);

    void idIsExists(Long id);
}
