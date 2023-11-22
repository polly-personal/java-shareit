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

@DisplayName("CommentDtoInTest должен ")
@JsonTest
public class CommentDtoInTest {
    @Autowired
    private JacksonTester<CommentDtoIn> json;
    CommentDtoIn commentDtoIn;

    @BeforeEach
    void initDto() {
        commentDtoIn =
                CommentDtoIn.builder().id(1L).text("test_text_1").authorName("test_authorName_1").build();
    }

    @DisplayName(" сериализовать dto в json")
    @Test
    void dtoTest() throws IOException {
        JsonContent<CommentDtoIn> result = json.write(commentDtoIn);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(commentDtoIn.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentDtoIn.getText());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(commentDtoIn.getAuthorName());
    }
}
