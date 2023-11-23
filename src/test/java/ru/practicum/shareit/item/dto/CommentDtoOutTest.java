package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CommentDtoOutTest должен ")
@JsonTest
public class CommentDtoOutTest {
    @Autowired
    private JacksonTester<CommentDtoOut> json;
    private CommentDtoOut commentDtoOut;

    @BeforeEach
    public void initDto() {
        commentDtoOut =
                CommentDtoOut.builder().id(1L).text("test_text_1").authorName("test_authorName_1").created(LocalDateTime.of(2023, 1, 1, 0, 0, 0)).build();
    }

    @DisplayName(" сериализовать dto в json")
    @Test
    public void dtoTest() throws IOException {
        JsonContent<CommentDtoOut> result = json.write(commentDtoOut);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(commentDtoOut.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentDtoOut.getText());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(commentDtoOut.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2023-01-01T00:00:00");
    }
}
