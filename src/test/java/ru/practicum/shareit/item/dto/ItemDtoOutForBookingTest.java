package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ItemDtoOutForBookingTest должен ")
@JsonTest
public class ItemDtoOutForBookingTest {
    @Autowired
    private JacksonTester<ItemDtoOutForBooking> json;
    ItemDtoOutForBooking itemDtoOutForBooking;

    @BeforeEach
    void initDto() {
        itemDtoOutForBooking =
                ItemDtoOutForBooking.builder().id(1L).name("test_name_1").description("test_description_1").available(true).build();
    }

    @DisplayName(" сериализовать dto в json")
    @Test
    void dtoTest() throws IOException {
        JsonContent<ItemDtoOutForBooking> result = json.write(itemDtoOutForBooking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemDtoOutForBooking.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDtoOutForBooking.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDtoOutForBooking.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDtoOutForBooking.getAvailable());
        assertThat(result).extractingJsonPathStringValue("$.owner").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.itemRequest").isEqualTo(null);
    }
}
