package ru.practicum.shareit.item.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId() != null ? item.getRequestId() : null
        );
        log.info("🔀 item: " + item + " сконвертирован в itemDto: " + itemDto);

        return itemDto;
    }

    public static List<ItemDto> toItemsDto(List<Item> items) {
        List<ItemDto> itemsDto = items
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());

        log.info("🔀 список items: " + items + " сконвертирован в itemsDto: " + itemsDto);

        return itemsDto;
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getRequestId() != null ? itemDto.getRequestId() : null
        );
        log.info("🔀 itemDto: " + itemDto + " сконвертирован в item: " + item);

        return item;
    }

    public static List<Item> toItems(List<ItemDto> itemsDto) {
        List<Item> items = itemsDto
                .stream()
                .map(ItemMapper::toItem)
                .collect(Collectors.toList());

        log.info("🔀 список itemsDto: " + itemsDto + " сконвертирован в items: " + items);

        return items;
    }
}
