package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@DisplayName("ItemRequestControllerTest должен ")
public class ItemRequestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @MockBean
    private UserService userService;

    private UserDto ownerDto;
    private UserDto requesterDto;
    private ItemDtoIn itemDtoIn;
    private ItemDtoOut itemDtoOut;
    private ItemRequestDtoIn itemRequestDtoIn;
    private ItemRequestDtoOut itemRequestDtoOut;

    @BeforeEach
    public void initEntities() {
        ownerDto = UserDto.builder().id(1L).name("test_name_1").email("test_email_1@gmail.com").build();
        requesterDto = UserDto.builder().id(2L).name("test_name_2").email("test_email_2@gmail.com").build();

        itemDtoIn = ItemDtoIn.builder().name("test_name_1").description("test_description_1").available(true).build();
        itemDtoOut = ItemDtoOut.builder().id(1L).name(itemDtoIn.getName()).description(itemDtoIn.getDescription()).available(itemDtoIn.getAvailable()).build();


        itemRequestDtoIn = ItemRequestDtoIn.builder().description("test_description_1").build();
        itemRequestDtoOut =
                ItemRequestDtoOut.builder().id(1L).description(itemRequestDtoIn.getDescription()).created(LocalDateTime.of(2023, 1, 1, 0, 0, 0)).requesterId(requesterDto.getId()).build();
    }

    @DisplayName("сохранять запрос_на_вещь")
    @Test
    public void create_whenSuccessInvoked_thenCreatedItemRequestIsReturned() throws Exception {
        when(itemRequestService.create(anyLong(), any())).thenReturn(itemRequestDtoOut);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", requesterDto.getId())
                        .content(mapper.writeValueAsString(itemRequestDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDtoOut.getDescription())))
                .andExpect(jsonPath("$.created").value("2023-01-01T00:00:00"))
                .andExpect(jsonPath("$.requesterId").value("2"));
    }

    @DisplayName("выдавать запрос_на_вещь по полю \"id\"")
    @Test
    public void getById_whenSuccessInvoked_thenIssuedItemRequestIsReturned() throws Exception {
        doNothing().when(userService).idIsExists(anyLong());
        when(itemRequestService.getById(anyLong())).thenReturn(itemRequestDtoOut);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", requesterDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemRequestDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDtoOut.getDescription())))
                .andExpect(jsonPath("$.created").value("2023-01-01T00:00:00"))
                .andExpect(jsonPath("$.requesterId").value("2"));
    }

    @DisplayName("выдавать все запросы_на_вещи по id владельца_запроса_вещи (requester.id) (выдача для владельца_запроса_вещи)")
    @Test
    public void getAllForRequester_whenSuccessInvoked_thenIssuedItemRequestsIsReturned() throws Exception {
        when(itemRequestService.getAllForRequester(anyLong())).thenReturn(List.of(itemRequestDtoOut));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", requesterDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequestDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDtoOut.getDescription())))
                .andExpect(jsonPath("$[0].created").value("2023-01-01T00:00:00"))
                .andExpect(jsonPath("$[0].requesterId").value("2"));
    }

    @DisplayName("выдавать все запросы_на_вещи по id пользователя (выдача для любого пользователя)")
    @Test
    public void getAllOtherUsersRequests_whenSuccessInvoked_thenIssuedItemRequestsIsReturned() throws Exception {
        when(itemRequestService.getAllOtherUsersRequests(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemRequestDtoOut));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", ownerDto.getId())
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequestDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDtoOut.getDescription())))
                .andExpect(jsonPath("$[0].created").value("2023-01-01T00:00:00"))
                .andExpect(jsonPath("$[0].requesterId").value("2"));
    }
}