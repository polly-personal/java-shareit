package ru.practicum.shareit.request.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.ItemRequestIdNotFoundException;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.exception.ThisUserAlreadyExistException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ItemRequestRepositoryImpl implements ItemRequestRepository {
    private final ItemRequestMapper itemRequestMapper;

    private Map<Long, ItemRequest> requests;
    private Long id;

    @Override
    public ItemRequestDto create(ItemRequest itemRequest) {
        if (requests == null) {
            requests = new HashMap<>();
        }

        if (!requests.containsValue(itemRequest)) {
            itemRequest.setId(getId());
            itemRequest.setCreated(LocalDateTime.now());

            Long id = itemRequest.getId();
            requests.put(id, itemRequest);
            log.info("🟩 пользователем создан запрос вещи (ItemRequest): " + itemRequest);

            return itemRequestMapper.toRequestDto(itemRequest);
        }

        log.info("🟩🟧 запрос вещи (ItemRequest) пользователем НЕ создан: " + itemRequest);
        throw new ThisUserAlreadyExistException("пользователь с id: " + itemRequest.getRequestor() + " уже создал запрос этой вещи");
    }

    @Override
    public void idIsExists(Long id) {
        if (requests != null && !requests.containsKey(id)) {
            throw new ItemRequestIdNotFoundException("введен несуществующий id запроса вещи (ItemRequest): " + id);
        }
    }

    private Long getId() {
        if (id == null) {
            id = 0L;
        }
        return ++id;
    }
}
