package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.constant.Status;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BookingDtoOutTest должен ")
@JsonTest
public class BookingDtoOutTest {
    @Autowired
    private JacksonTester<BookingDtoOut> json;
    BookingDtoOut bookingDtoOut;

    @BeforeEach
    void initUserDto() {
        bookingDtoOut =
                BookingDtoOut.builder().id(1L).start(LocalDateTime.of(2023, 1,
                        1, 0, 0, 0)).end(LocalDateTime.of(2023, 1, 2, 0, 0, 0)).status(Status.WAITING).build();
    }

    @DisplayName(" сериализовать dto в json")
    @Test
    void itemDtoInTest() throws IOException {
        JsonContent<BookingDtoOut> result = json.write(bookingDtoOut);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(bookingDtoOut.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-01-01T00:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-01-02T00:00:00");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }
}
