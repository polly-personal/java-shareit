package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.exception.ItemRequestIdNotFound;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserService userService;

    private final ItemRequestRepository itemRequestRepository;

    @Transactional
    @Override
    public ItemRequestDtoOut create(long userId, ItemRequestDtoIn itemRequestDtoIn) {
        UserDto userDto = userService.getById(userId);

        ItemRequest itemRequest = ItemRequestMapper.toItemRequestForItem(itemRequestDtoIn);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(UserMapper.toUser(userDto));
        itemRequest.setItems(new ArrayList<>());
        ItemRequest createdItemRequest = itemRequestRepository.save(itemRequest);
        log.info("🟩 пользователем создан запрос вещи (ItemRequest): " + itemRequest);

        return ItemRequestMapper.toItemRequestDtoOut(createdItemRequest);
    }

    @Override
    public ItemRequestDtoOut getById(Long itemRequestId) {
        ItemRequest request =
                itemRequestRepository.findById(itemRequestId).orElseThrow(() ->
                        new ItemRequestIdNotFound("введен несуществующий id запроса вещи (itemRequestId): " + itemRequestId));


        ItemRequestDtoOut requestDtoOut = ItemRequestMapper.toItemRequestDtoOut(request);

        log.info("🟦 выдан запрос на вещь: " + requestDtoOut + "\nот запрашивающего с id: " + itemRequestId);
        return requestDtoOut;
    }

    @Override
    public List<ItemRequestDtoOut> getAllForRequester(long userId) {
        userService.idIsExists(userId);
        List<ItemRequest> requests = itemRequestRepository.findAllByRequesterId(userId);

        List<ItemRequestDtoOut> requestsDtoOut = ItemRequestMapper.toItemRequestsDtoOut(requests);

        log.info("🟦 выданы все запросы на вещи: " + requestsDtoOut + "\nот запрашивающего с id: " + userId);
        return requestsDtoOut;
    }

    @Override
    public List<ItemRequestDtoOut> getAllOtherUsersRequests(long userId, int from, int size) {
        userService.idIsExists(userId);

        PageRequest pageRequest = PageRequest.of(from, size, Sort.by("created").descending());
        List<ItemRequest> requests = itemRequestRepository.findAllWithoutRequester(userId, pageRequest);

        List<ItemRequestDtoOut> requestsDtoOut = ItemRequestMapper.toItemRequestsDtoOut(requests);

        log.info("🟦 выданы все запросы на вещи: " + requestsDtoOut +
                "\nот всех пользователей " +
                "\n(для запрашивающего с id: " + userId + ")");
        return requestsDtoOut;
    }

    @Override
    public void idIsExists(Long id) {
        if (id != null && !itemRequestRepository.existsById(id)) {
            throw new ItemRequestIdNotFound("\"введен несуществующий id запроса вещи (itemRequestId): \" + id");
        }
    }
}
