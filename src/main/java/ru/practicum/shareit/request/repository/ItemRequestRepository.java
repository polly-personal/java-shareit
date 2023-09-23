package ru.practicum.shareit.request.repository;

import ru.practicum.shareit.request.model.ItemRequest;

public interface ItemRequestRepository {
    ItemRequest create(ItemRequest itemRequest);

    boolean idIsExists(Long id);

    boolean itemRequestIsExists(ItemRequest itemRequest);
}
