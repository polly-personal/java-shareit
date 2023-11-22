package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ItemRequestDtoOutTest должен ")
@JsonTest
public class ItemRequestDtoOutTest {
    @Autowired
    private JacksonTester<ItemRequestDtoOut> json;
    ItemRequestDtoOut itemRequestDtoOut;

    @BeforeEach
    void initDto() {
        itemRequestDtoOut =
                ItemRequestDtoOut.builder().id(1L).description("test_description_1").created(LocalDateTime.of(2023, 1,
                        1, 0, 0, 0)).requesterId(1L).build();
    }

    @DisplayName(" сериализовать dto в json")
    @Test
    void dtoTest() throws IOException {
        JsonContent<ItemRequestDtoOut> result = json.write(itemRequestDtoOut);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemRequestDtoOut.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestDtoOut.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2023-01-01T00:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.requesterId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.items").isEqualTo(null);
    }
}
