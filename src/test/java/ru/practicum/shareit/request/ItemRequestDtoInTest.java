package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ItemRequestDtoInTest должен ")
@JsonTest
public class ItemRequestDtoInTest {
    @Autowired
    private JacksonTester<ItemRequestDtoIn> json;
    private ItemRequestDtoIn itemRequestDtoIn;

    @BeforeEach
    public void initDto() {
        itemRequestDtoIn =
                ItemRequestDtoIn.builder().description("test_description_1").build();
    }

    @DisplayName(" сериализовать dto в json")
    @Test
    public void dtoTest() throws IOException {
        JsonContent<ItemRequestDtoIn> result = json.write(itemRequestDtoIn);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestDtoIn.getDescription());
    }
}
