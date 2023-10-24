package ru.practicum.shareit.request.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ItemRequestMapper {

    public static ItemRequestDto toRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
//                .requestor(itemRequest.getRequestor())
                .created(itemRequest.getCreated())
                .build();
//                new ItemRequestDto(
//                itemRequest.getId(),
//                itemRequest.getDescription(),
//                itemRequest.getRequestor(),
//                itemRequest.getCreated()
//        );
        log.info("🔀 itemRequest: " + itemRequest + " сконвертирован в itemRequestDto: " + itemRequestDto);

        return itemRequestDto;
    }

    public static List<ItemRequestDto> toItemRequestsDto(List<ItemRequest> itemRequests) {
        List<ItemRequestDto> itemRequestsDto = itemRequests
                .stream()
                .map(ItemRequestMapper::toRequestDto)
                .collect(Collectors.toList());

        log.info("🔀 список itemRequests: " + itemRequests + " сконвертирован в itemRequestsDto: " + itemRequestsDto);

        return itemRequestsDto;
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .created(itemRequestDto.getCreated())
//                .requestor(itemRequestDto.getRequestor())
                .build();
//                new ItemRequest(
//                itemRequestDto.getId(),
//                itemRequestDto.getDescription(),
//                itemRequestDto.getCreated(),
//                itemRequestDto.getRequestor()
//        );
        log.info("🔀 itemRequestDto: " + itemRequestDto + " сконвертирован в itemRequest: " + itemRequest);

        return itemRequest;
    }

    public static List<ItemRequest> toItemRequests(List<ItemRequestDto> itemRequestDto) {
        List<ItemRequest> itemRequests = itemRequestDto
                .stream()
                .map(ItemRequestMapper::toItemRequest)
                .collect(Collectors.toList());

        log.info("🔀 список itemRequestDto: " + itemRequestDto + " сконвертирован в itemRequests: " + itemRequests);

        return itemRequests;
    }
}
