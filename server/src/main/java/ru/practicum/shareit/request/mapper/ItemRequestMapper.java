package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class ItemRequestMapper {

    public ItemRequestDtoOut toItemRequestDtoOut(ItemRequest itemRequest) {
        ItemRequestDtoOut itemRequestDtoOut = ItemRequestDtoOut.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .requesterId(itemRequest.getRequester().getId())
                .items(ItemMapper.toItemsDtoOut(itemRequest.getItems()))
                .build();

        log.info("🔀 itemRequest: " + itemRequest + " сконвертирован в itemRequestDtoOut: " + itemRequestDtoOut);
        return itemRequestDtoOut;
    }

    public List<ItemRequestDtoOut> toItemRequestsDtoOut(List<ItemRequest> itemRequests) {
        List<ItemRequestDtoOut> itemRequestsDtoOut = itemRequests
                .stream()
                .map(ItemRequestMapper::toItemRequestDtoOut)
                .collect(Collectors.toList());

        log.info("🔀 список itemRequests: " + itemRequests + " сконвертирован в itemRequestsDtoOut: " + itemRequestsDtoOut);
        return itemRequestsDtoOut;
    }

    public ItemRequestDtoIn toRequestDtoIn(ItemRequest itemRequest) {
        ItemRequestDtoIn itemRequestDtoIn = ItemRequestDtoIn.builder()
                .description(itemRequest.getDescription())
                .build();

        log.info("🔀 itemRequest: " + itemRequest + " сконвертирован в itemRequestDtoIn: " + itemRequestDtoIn);
        return itemRequestDtoIn;
    }

    public ItemRequest toItemRequestForItem(ItemRequestDtoIn itemRequestDtoIn) {
        ItemRequest itemRequest = ItemRequest.builder()
                .description(itemRequestDtoIn.getDescription())
                .build();

        log.info("🔀 itemRequestDtoIn: " + itemRequestDtoIn + " сконвертирован в itemRequest: " + itemRequest);
        return itemRequest;
    }

    public List<ItemRequest> toItemRequests(List<ItemRequestDtoIn> itemRequestDtoIn) {
        List<ItemRequest> itemRequests = itemRequestDtoIn
                .stream()
                .map(ItemRequestMapper::toItemRequestForItem)
                .collect(Collectors.toList());

        log.info("🔀 список itemRequestDtoIn: " + itemRequestDtoIn + " сконвертирован в itemRequests: " + itemRequests);
        return itemRequests;
    }

    public ItemRequest toItemRequestForItem(ItemRequestDtoOut itemRequestDtoOut) {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(itemRequestDtoOut.getId())
                .build();

        log.info("🔀 itemRequestDtoOut: " + itemRequestDtoOut + " сконвертирован в itemRequest: " + itemRequest);
        return itemRequest;
    }
}
