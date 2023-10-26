package ru.practicum.shareit.item.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.ItemDtoOutForBooking;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ItemMapper {
    public static ItemDtoOut toItemDtoOut(Item item) {
        ItemDtoOut itemDtoOut = ItemDtoOut.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();

        log.info("🔀 item: " + item + " сконвертирован в giveAndGetItemDto: " + itemDtoOut);
        return itemDtoOut;
    }

    public static ItemDtoOutForBooking toItemDtoOutForBooking(Item item) {
        ItemDtoOutForBooking itemDtoOutForBooking = ItemDtoOutForBooking.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .itemRequest(item.getItemRequest())
                .build();

        log.info("🔀 item: " + item + " сконвертирован в itemDtoOutForBooking: " + itemDtoOutForBooking);
        return itemDtoOutForBooking;
    }

    public static List<ItemDtoOut> toItemsDtoOut(List<Item> items) {
        List<ItemDtoOut> itemsDto = items
                .stream()
                .map(ItemMapper::toItemDtoOut)
                .collect(Collectors.toList());

        log.info("🔀 список items: " + items + " сконвертирован в itemsDto: " + itemsDto);
        return itemsDto;
    }

    public static Item toItem(ItemDtoIn itemDtoIn) {
        Item item = Item.builder()
                .id(itemDtoIn.getId())
                .name(itemDtoIn.getName())
                .description(itemDtoIn.getDescription())
                .available(itemDtoIn.getAvailable())
                .build();

        log.info("🔀 itemDto: " + itemDtoIn + " сконвертирован в item: " + item);
        return item;
    }

    public static Item toItemDtoOutForBooking(ItemDtoOutForBooking itemDtoOutForBooking) {
        Item item = Item.builder()
                .id(itemDtoOutForBooking.getId())
                .name(itemDtoOutForBooking.getName())
                .description(itemDtoOutForBooking.getDescription())
                .available(itemDtoOutForBooking.getAvailable())
                .owner(itemDtoOutForBooking.getOwner())
                .itemRequest(itemDtoOutForBooking.getItemRequest())
                .build();

        log.info("🔀 itemDtoOutForBooking: " + itemDtoOutForBooking + " сконвертирован в item: " + item);
        return item;
    }

    public static List<Item> toItems(List<ItemDtoIn> itemsDtoIn) {
        List<Item> items = itemsDtoIn
                .stream()
                .map(ItemMapper::toItem)
                .collect(Collectors.toList());

        log.info("🔀 список itemsDtoIn: " + itemsDtoIn + " сконвертирован в items: " + items);
        return items;
    }
}
