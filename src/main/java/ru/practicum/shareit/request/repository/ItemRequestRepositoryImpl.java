package ru.practicum.shareit.request.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.exception.ItemRequestIdNotFound;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ItemRequestRepositoryImpl implements ItemRequestRepository {
    private Map<Long, ItemRequest> requests = new HashMap<>();
    private Long id;

    @Override
    public ItemRequest create(ItemRequest itemRequest) {
        itemRequest.setId(getId());
        itemRequest.setCreated(LocalDateTime.now());

        requests.put(itemRequest.getId(), itemRequest);

        return itemRequest;
    }

    @Override
    public boolean idIsExists(Long id) {
        if (!requests.containsKey(id)) {
            throw new ItemRequestIdNotFound("введен несуществующий id запроса вещи (ItemRequest): " + id);
        }
        return requests.containsKey(id);
    }

    @Override
    public boolean itemRequestIsExists(ItemRequest itemRequest) {
        return requests.containsValue(itemRequest);
    }

    private Long getId() {
        if (id == null) {
            id = 0L;
        }
        return ++id;
    }
}
