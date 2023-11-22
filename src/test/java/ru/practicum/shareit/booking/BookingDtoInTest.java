package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoIn;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BookingDtoInTest должен ")
@JsonTest
public class BookingDtoInTest {
    @Autowired
    private JacksonTester<BookingDtoIn> json;
    BookingDtoIn bookingDtoIn;

    @BeforeEach
    void initDto() {
        bookingDtoIn =
                BookingDtoIn.builder().id(1L).start(LocalDateTime.of(2023, 1,
                        1, 0, 0, 0)).end(LocalDateTime.of(2023, 1, 2, 0, 0, 0)).itemId(1L).build();
    }

    @DisplayName(" сериализовать dto в json")
    @Test
    void dtoTest() throws IOException {
        JsonContent<BookingDtoIn> result = json.write(bookingDtoIn);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(bookingDtoIn.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-01-01T00:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-01-02T00:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }
}
