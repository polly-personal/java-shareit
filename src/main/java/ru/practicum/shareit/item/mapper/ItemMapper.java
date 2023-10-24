package ru.practicum.shareit.item.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.GiveItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ItemMapper {
    public static GiveItemDto toItemDto(Item item) {
        GiveItemDto giveItemDto = GiveItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
//                item.getRequestId() != null ? item.getRequestId() : null
                .build();
//        );
        log.info("🔀 item: " + item + " сконвертирован в itemDto: " + giveItemDto);

        return giveItemDto;
    }

    public static List<GiveItemDto> toItemsDto(List<Item> items) {
        List<GiveItemDto> itemsDto = items
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());

        log.info("🔀 список items: " + items + " сконвертирован в itemsDto: " + itemsDto);

        return itemsDto;
    }

    public static Item toItem(GiveItemDto giveItemDto) {
        Item item = Item.builder()
                .id(giveItemDto.getId())
                .name(giveItemDto.getName())
                .description(giveItemDto.getDescription())
                .available(giveItemDto.getAvailable())
                .build();
//                new Item(
//                itemDto.getId(),
//                itemDto.getName(),
//                itemDto.getDescription(),
//                itemDto.getAvailable()
////                itemDto.getRequestId() != null ? itemDto.getRequestId() : null
//        );
        log.info("🔀 itemDto: " + giveItemDto + " сконвертирован в item: " + item);

        return item;
    }

    public static List<Item> toItems(List<GiveItemDto> itemsDto) {
        List<Item> items = itemsDto
                .stream()
                .map(ItemMapper::toItem)
                .collect(Collectors.toList());

        log.info("🔀 список itemsDto: " + itemsDto + " сконвертирован в items: " + items);

        return items;
    }
}
