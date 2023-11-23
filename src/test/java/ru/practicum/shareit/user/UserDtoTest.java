package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserDtoTest должен ")
@JsonTest
public class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> json;
    private UserDto userDto;

    @BeforeEach
    public void initDto() {
        userDto = UserDto.builder().id(1L).name("test_name_1").email("test_email_1").build();
    }

    @DisplayName(" сериализовать dto в json")
    @Test
    public void dtoTest() throws IOException {
        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(userDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userDto.getEmail());
    }
}
