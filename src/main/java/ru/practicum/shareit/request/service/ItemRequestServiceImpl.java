package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.exception.ItemRequestIdNotFound;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserThisUserAlreadyExist;
import ru.practicum.shareit.user.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto create(long userId, ItemRequestDto itemRequestDto) {
        idIsExists(userId);

        ItemRequest itemRequestFromDto = ItemRequestMapper.toItemRequest(itemRequestDto);
        /*itemRequestFromDto.setRequestor(userId);*/

        if (!itemRequestRepository.itemRequestIsExists(itemRequestFromDto)) {
            ItemRequest createdItemRequest = itemRequestRepository.create(itemRequestFromDto);
            log.info("🟩 пользователем создан запрос вещи (ItemRequest): " + itemRequestFromDto);

            return ItemRequestMapper.toRequestDto(createdItemRequest);
        }

        log.info("🟩🟧 запрос вещи (ItemRequest) пользователем НЕ создан: " + itemRequestDto);

        throw new UserThisUserAlreadyExist("пользователь с id: " + itemRequestDto.getRequestor() + " уже создал запрос точно такой же вещи");
    }

    @Override
    public void idIsExists(Long id) {
        if (id != null && !userRepository.existsById(id)) {
            throw new ItemRequestIdNotFound("введен несуществующий id запроса вещи (ItemRequest): " + id);
        }
    }
}
