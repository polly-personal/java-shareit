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

@DisplayName("ItemDtoInTest должен ")
@JsonTest
public class ItemDtoInTest {
    @Autowired
    private JacksonTester<ItemDtoIn> json;
    private ItemDtoIn itemDtoIn;

    @BeforeEach
    public void initDto() {
        itemDtoIn =
                ItemDtoIn.builder().id(1L).name("test_name_1").description("test_description_1").available(true).requestId(1L).build();
    }

    @DisplayName(" сериализовать dto в json")
    @Test
    public void dtoTest() throws IOException {
        JsonContent<ItemDtoIn> result = json.write(itemDtoIn);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemDtoIn.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDtoIn.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDtoIn.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDtoIn.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(itemDtoIn.getRequestId().intValue());
    }
}
