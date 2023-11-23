package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@DisplayName("ItemControllerTest должен ")
public class ItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    private UserDto ownerDto;
    private ItemDtoIn itemDtoIn;
    private ItemDtoOut itemDtoOut;

    @BeforeEach
    public void initEntities() {
        ownerDto = UserDto.builder().id(1L).name("test_name_1").email("test_email_1@gmail.com").build();
        itemDtoIn = ItemDtoIn.builder().name("test_name_1").description("test_description_1").available(true).build();
        itemDtoOut =
                ItemDtoOut.builder().id(1L).name(itemDtoIn.getName()).description(itemDtoIn.getDescription()).available(itemDtoIn.getAvailable()).build();
    }

    @DisplayName("сохранять предмет")
    @Test
    public void create_whenSuccessInvoked_thenCreatedItemIsReturned() throws Exception {
        when(itemService.create(anyLong(), any())).thenReturn(itemDtoOut);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", ownerDto.getId())
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoOut.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoOut.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoOut.getAvailable())));
    }

    @DisplayName("НЕ сохранять предмет, если этот id меньше или равен нулю")
    @Test
    public void create_whenConstraintViolationException_thenCreatedItemIsNotReturned() throws Exception {
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", -1L)
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
    }

    @DisplayName("НЕ сохранять предмет, если в пришедшем json'e поле \"available\" == null")
    @Test
    public void create_whenMethodArgumentNotValidException_thenCreatedItemIsNotReturned() throws Exception {
        itemDtoIn.setAvailable(null);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", ownerDto.getId())
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @DisplayName("обновлять предмет по полю \"id\"")
    @Test
    public void updateById_whenSuccessInvoked_thenUpdatedItemIsReturned() throws Exception {
        ItemDtoIn updatedItemDtoIn = ItemDtoIn.builder().name("test_update-name_1").build();
        itemDtoOut.setName(updatedItemDtoIn.getName());
        when(itemService.updateById(anyLong(), anyLong(), any())).thenReturn(itemDtoOut);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", ownerDto.getId())
                        .content(mapper.writeValueAsString(updatedItemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoOut.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoOut.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoOut.getAvailable())));
    }

    @DisplayName("удалять предмет по полю \"id\"")
    @Test
    public void deleteById_whenSuccessInvoked_thenStringResponseIsReturned() throws Exception {
        String response = "⬛️ удален предмет по id: 1";
        when(itemService.deleteById(anyLong())).thenReturn(response);

        mvc.perform(delete("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("выдавать предмет по полю \"id\"")
    @Test
    public void getById_whenSuccessInvoked_thenIssuedItemIsReturned() throws Exception {
        when(itemService.getById(anyLong(), anyLong())).thenReturn(itemDtoOut);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", ownerDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoOut.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoOut.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoOut.getAvailable())));
    }

    @DisplayName("выдавать всех пользователей по полю \"owner.id\"")
    @Test
    public void getAllByOwnerId_whenSuccessInvoked_thenIssuedItemsIsReturned() throws Exception {
        when(itemService.getAllByOwnerId(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemDtoOut));

        /* /items?from=0&size=10 */
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", ownerDto.getId())
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDtoOut.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDtoOut.getDescription())));
    }

    @DisplayName("выдавать все предметы по поиску содеражщегося слова в полях \"name\" и \"description\"")
    @Test
    public void searchForUserByParameter_whenSuccessInvoked_thenIssuedItemsIsReturned() throws Exception {
        when(itemService.searchForUserByParameter(anyString(), anyInt(), anyInt())).thenReturn(List.of(itemDtoOut));

        /* /items?from=0&size=10 */
        mvc.perform(get("/items/search")
                        .param("text", "test")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDtoOut.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDtoOut.getDescription())));
    }

    @DisplayName("НЕ выдавать все предметы по поиску содеражщегося слова в полях \"name\" и \"description\", если параметр запроса \"from\" меньше 0")
    @Test
    public void searchForUserByParameter_whenConstraintViolationException_thenIssuedItemsIsNotReturned() throws Exception {
        /* /items?from=0&size=10 */
        mvc.perform(get("/items/search")
                        .param("text", "test")
                        .param("from", "-1")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("searchForUserByParameter.from: must be greater than or equal to 0", result.getResolvedException().getMessage()));
    }

    @DisplayName("сохранять комментарий по id пользователя и предмета")
    @Test
    public void createComment_whenSuccessInvoked_thenCreatedCommentIsReturned() throws Exception {
        CommentDtoIn commentDtoIn = CommentDtoIn.builder().text("test_text_1").build();

        UserDto bookerDto = UserDto.builder().id(2L).name("test_name_2").email("test_email_2@gmail.com").build();
        CommentDtoOut commentDtoOut = CommentDtoOut.builder().id(1L).text("test_text_1").authorName(bookerDto.getName()).created(LocalDateTime.of(2023, 1, 1, 0, 0, 0)).build();

        when(itemService.createComment(anyLong(), anyLong(), any())).thenReturn(commentDtoOut);

        mvc.perform(post("/items/1/comment") //
                        .header("X-Sharer-User-Id", bookerDto.getId())
                        .content(mapper.writeValueAsString(commentDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDtoOut.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDtoOut.getAuthorName())))
                .andExpect(jsonPath("$.created").value("2023-01-01T00:00:00"));
    }

    @DisplayName("НЕ сохранять комментарий по id пользователя и предмета, если в пришедшем json'e поле \"text\" == null")
    @Test
    public void createComment_whenSuccessInvoked_thenCreatedCommentIsReturned2() throws Exception {
        CommentDtoIn commentDtoIn = CommentDtoIn.builder().text(null).build();

        UserDto bookerDto = UserDto.builder().id(2L).name("test_name_2").email("test_email_2@gmail.com").build();
        CommentDtoOut commentDtoOut = CommentDtoOut.builder().id(1L).text("test_text_1").authorName(bookerDto.getName()).created(LocalDateTime.of(2023, 1, 1, 0, 0, 0)).build();

        when(itemService.createComment(anyLong(), anyLong(), any())).thenReturn(commentDtoOut);

        mvc.perform(post("/items/1/comment") //
                        .header("X-Sharer-User-Id", bookerDto.getId())
                        .content(mapper.writeValueAsString(commentDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
}