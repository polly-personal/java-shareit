package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.booking.constant.Status;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@DisplayName("BookingControllerTest должен ")
public class BookingControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;
    @MockBean
    private UserService userService;


    private UserDto ownerDto;
    private UserDto bookerDto;
    private BookingDtoOut.Booker bookerFromBookingDtoOut;


    private ItemDtoOut itemDtoOut;
    private BookingDtoOut.Item itemFromBookingDtoOut;

    private BookingDtoIn currentBookingDtoIn;
    private BookingDtoOut currentBookingDtoOut;

    @BeforeEach
    public void initEntities() {
        ownerDto = UserDto.builder()
                .id(1L)
                .name("test_name_1")
                .email("test_email_1@gmail.com")
                .build();
        bookerDto = UserDto.builder()
                .id(2L)
                .name("test_name_2")
                .email("test_email_2@gmail.com")
                .build();
        bookerFromBookingDtoOut = new BookingDtoOut.Booker(bookerDto.getId());


        itemDtoOut = ItemDtoOut.builder()
                .id(1L)
                .name("test_name_1")
                .description("test_description_1")
                .available(true)
                .build();
        itemFromBookingDtoOut = new BookingDtoOut.Item(itemDtoOut.getId(), itemDtoOut.getName());

        currentBookingDtoIn = BookingDtoIn.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(9))
                .itemId(itemDtoOut.getId())
                .build();
        currentBookingDtoOut = BookingDtoOut.builder()
                .id(1L)
                .start(currentBookingDtoIn.getStart())
                .end(currentBookingDtoIn.getEnd())
                .status(Status.WAITING)
                .booker(bookerFromBookingDtoOut)
                .item(itemFromBookingDtoOut).build();
    }

    @DisplayName("сохранять бронь по id бронирующего")
    @Test
    public void create_whenSuccessInvoked_thenCreatedBookingIsReturned() throws Exception {
        when(bookingService.create(anyLong(), any())).thenReturn(currentBookingDtoOut);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", bookerDto.getId())
                        .content(mapper.writeValueAsString(currentBookingDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(currentBookingDtoOut.getId()), Long.class));
    }

    @DisplayName("НЕ сохранять бронь по id бронирующего, если этот id меньше или равен нулю")
    @Test
    public void create_whenConstraintViolationException_thenCreatedBookingIsNotReturned() throws Exception {
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", -1L)
                        .content(mapper.writeValueAsString(currentBookingDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
    }

    @DisplayName("НЕ сохранять бронь по id бронирующего, если в пришедшем json'e поле \"start\" в прошлом")
    @Test
    public void create_whenMethodArgumentNotValidException_thenCreatedBookingIsNotReturned() throws Exception {
        currentBookingDtoIn.setStart(LocalDateTime.now().minusDays(1));

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", bookerDto.getId())
                        .content(mapper.writeValueAsString(currentBookingDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @DisplayName("обновлять статус_брони по id владельца_вещи и самой брони")
    @Test
    public void updateStatus_whenSuccessInvoked_thenUpdatedBookingIsReturned() throws Exception {
        currentBookingDtoOut.setStatus(Status.APPROVED);
        when(bookingService.updateStatus(anyLong(), anyLong(), anyBoolean())).thenReturn(currentBookingDtoOut);

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", ownerDto.getId())
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(currentBookingDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.status").value("APPROVED"));

    }

    @DisplayName("выдавать бронь по id владельца_вещи или бронирующего_вещь и по id самой брони")
    @Test
    public void getByIdAndOwnerOrBookerId_whenSuccessInvoked_thenBookingIsReturned() throws Exception {
        when(bookingService.getByIdAndOwnerOrBookerId(anyLong(), anyLong())).thenReturn(currentBookingDtoOut);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", ownerDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(currentBookingDtoOut.getId()), Long.class));
    }

    @DisplayName("выдавать ALL брони по id бронирующего_вещь")
    @Test
    public void getAllByBookerId_whenSuccessInvoked_thenAllBookingsIsReturned() throws Exception {
        when(bookingService.getAllByBookerId(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(currentBookingDtoOut));

        /* /bookings?state=ALL&from=0&size=10 */
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", bookerDto.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(currentBookingDtoOut.getId()), Long.class));
    }

    @DisplayName("НЕ выдавать ALL брони по id бронирующего_вещь, если параметр запроса \"from\" меньше 0")
    @Test
    public void getAllByBookerId_whenConstraintViolationException_thenAllBookingsIsNotReturned2() throws Exception {
        /* /bookings?state=ALL&from=0&size=10 */
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", bookerDto.getId())
                        .param("state", "ALL")
                        .param("from", "-1")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("getAllByBookerId.from: must be greater than or equal to 0", result.getResolvedException().getMessage()));
    }

    @DisplayName("выдавать ALL брони по id владельца_вещи")
    @Test
    void getAllByOwnerId_whenSuccessInvoked_thenAllBookingsIsReturned() throws Exception {
        doNothing().when(userService).idIsExists(anyLong());
        when(bookingService.getAllByOwnerId(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(currentBookingDtoOut));

        /* /bookings?state=ALL&from=0&size=10 */
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", bookerDto.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @DisplayName("НЕ выдавать ALL брони по id владельца_вещи, если параметр запроса \"size\" меньше или равен 0")
    @Test
    void getAllByOwnerId_whenConstraintViolationException_thenAllBookingsIsNotReturned() throws Exception {
        /* /bookings?state=ALL&from=0&size=10 */
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", bookerDto.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "0")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
    }
}