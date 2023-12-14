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

@DisplayName("ItemDtoOutTest должен ")
@JsonTest
public class ItemDtoOutTest {
    @Autowired
    private JacksonTester<ItemDtoOut> json;
    private ItemDtoOut itemDtoOut;

    @BeforeEach
    public void initDto() {
        itemDtoOut =
                ItemDtoOut.builder().id(1L).name("test_name_1").description("test_description_1").available(true).requestId(1L).build();
    }

    @DisplayName(" сериализовать dto в json")
    @Test
    public void dtoTest() throws IOException {
        JsonContent<ItemDtoOut> result = json.write(itemDtoOut);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemDtoOut.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDtoOut.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDtoOut.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDtoOut.getAvailable());
        assertThat(result).extractingJsonPathStringValue("$.lastBooking").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.nextBooking").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.comments").isEqualTo(null);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(itemDtoOut.getRequestId().intValue());
    }
}
