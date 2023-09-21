package ru.practicum.shareit.request.repository;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

public interface ItemRequestRepository {
    ItemRequestDto create(ItemRequest itemRequest);

    void idIsExists(Long id);
}
