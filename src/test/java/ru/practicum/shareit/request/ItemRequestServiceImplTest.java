package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.exception.ItemRequestIdNotFound;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.exception.UserIdNotFound;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("ItemRequestServiceImplTest должен ")
public class ItemRequestServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestServiceImpl;

    User owner;
    User requester;
    Item item;
    ItemRequest itemRequest;
    int from = 0;
    int size = 10;
    PageRequest pageRequest = PageRequest.of(from, size, Sort.by("created").descending());

    @BeforeEach
    void initEntities() {
        owner = User.builder().id(1L).name("test_name_1").email("test_email_1").build();
        requester = User.builder().id(2L).name("test_name_2").email("test_email_2").build();
        item = Item.builder().id(1L).name("test_name_1").description("test_description_1").available(true).owner(owner).build();
        itemRequest = ItemRequest.builder().id(1L).description("test_description_1").created(LocalDateTime.of(2023, 1, 1, 00, 00)).items(new ArrayList<>()).requester(requester).build();
    }

    @DisplayName("сохранять запрос_на_вещь по id пользователя")
    @Test
    void create_whenSuccessInvoked_thenCreatedItemRequestIsReturned() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.toUserDto(requester));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);

        ItemRequestDtoIn itemRequestDtoIn = ItemRequestMapper.toRequestDtoIn(itemRequest);
        ItemRequestDtoOut returnedItemRequestDtoOut = itemRequestServiceImpl.create(anyLong(), itemRequestDtoIn);

        ItemRequestDtoOut itemRequestDtoOut = ItemRequestMapper.toItemRequestDtoOut(itemRequest);
        Assertions.assertEquals(itemRequestDtoOut, returnedItemRequestDtoOut);
    }

    @DisplayName("НЕ сохранять запрос_на_вещь по id пользователя, если этот id НЕ найден в бд")
    @Test
    void create_whenIdNotFound_thenCreatedItemRequestIsNotReturned() {
        when(userService.getById(anyLong())).thenThrow(UserIdNotFound.class);

        ItemRequestDtoIn itemRequestDtoIn = ItemRequestMapper.toRequestDtoIn(itemRequest);
        Assertions.assertThrows(UserIdNotFound.class, () -> itemRequestServiceImpl.create(anyLong(), itemRequestDtoIn));
    }

    @DisplayName("выдавать запрос_на_вещь по полю \"id\" (выдача для владельца_запроса_вещи)")
    @Test
    void getById_whenSuccessInvoked_thenIssuedItemRequestIsReturned() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));

        ItemRequestDtoOut returnedItemRequestDtoOut = itemRequestServiceImpl.getById(anyLong());

        ItemRequestDtoOut itemRequestDtoOut = ItemRequestMapper.toItemRequestDtoOut(itemRequest);
        Assertions.assertEquals(itemRequestDtoOut, returnedItemRequestDtoOut);
    }

    @DisplayName("НЕ выдавать запрос_на_вещь по полю \"id\", если этот id НЕ найден в бд (выдача для владельца_запроса_вещи)")
    @Test
    void getById_whenIdNotFound_thenIssuedItemRequestIsNotReturned() {
        when(itemRequestRepository.findById(anyLong())).thenThrow(ItemRequestIdNotFound.class);

        Assertions.assertThrows(ItemRequestIdNotFound.class, () -> itemRequestServiceImpl.getById(anyLong()));
    }

    @DisplayName("выдавать все запросы_на_вещи по id владельца_запроса_вещи (requester.id) (выдача для " +
            "владельца_запроса_вещи)")
    @Test
    void getAllForRequester_whenSuccessInvoked_thenIssuedItemRequestIsReturned() {
        doNothing().when(userService).idIsExists(anyLong());
        when(itemRequestRepository.findAllByRequesterId(anyLong())).thenReturn(List.of(itemRequest));

        List<ItemRequestDtoOut> returnedItemRequestsDtoOut = itemRequestServiceImpl.getAllForRequester(anyLong());

        List<ItemRequestDtoOut> itemRequestsDtoOut = ItemRequestMapper.toItemRequestsDtoOut(List.of(itemRequest));
        Assertions.assertEquals(itemRequestsDtoOut, returnedItemRequestsDtoOut);
    }

    @DisplayName("НЕ выдавать все запросы_на_вещи по id владельца_запроса_вещи (requester.id), если этот id НЕ найден" +
            " в бд (выдача для владельца_запроса_вещи)")
    @Test
    void getAllForRequester_whenIdNotFound_thenIssuedItemRequestIsNotReturned() {
        doThrow(ItemRequestIdNotFound.class).when(userService).idIsExists(anyLong());

        Assertions.assertThrows(ItemRequestIdNotFound.class, () -> itemRequestServiceImpl.getAllForRequester(anyLong()));
    }

    @DisplayName("выдавать все запросы_на_вещи по id пользователя (выдача для обычного пользователя)")
    @Test
    void getAllOtherUsersRequests_whenSuccessInvoked_thenIssuedItemRequestIsReturned() {
        doNothing().when(userService).idIsExists(owner.getId());
        when(itemRequestRepository.findAllWithoutRequester(owner.getId(), pageRequest)).thenReturn(List.of(itemRequest));

        List<ItemRequestDtoOut> returnedItemRequestsDtoOut =
                itemRequestServiceImpl.getAllOtherUsersRequests(owner.getId(), from, size);

        List<ItemRequestDtoOut> itemRequestsDtoOut = ItemRequestMapper.toItemRequestsDtoOut(List.of(itemRequest));
        Assertions.assertEquals(itemRequestsDtoOut, returnedItemRequestsDtoOut);
    }

    @DisplayName("НЕ выдавать все запросы_на_вещи по id пользователя (выдача для обычного пользователя), если этот id НЕ найден в бд")
    @Test
    void getAllOtherUsersRequests_whenIdNotFound_thenIssuedItemRequestNotIsReturned() {
        doThrow(ItemRequestIdNotFound.class).when(userService).idIsExists(anyLong());

        Assertions.assertThrows(ItemRequestIdNotFound.class, () -> itemRequestServiceImpl.getAllOtherUsersRequests(owner.getId(), from, size));
    }

    @DisplayName("проверять запрос_на_вещь на наличие в бд по полю \"id\" (результат: найден)")
    @Test
    void idIsExists_whenSuccessInvoked_thenExceptionIsNotReturned() {
        when(itemRequestRepository.existsById(anyLong())).thenReturn(true);

        itemRequestServiceImpl.idIsExists(anyLong());
    }

    @DisplayName("проверять запрос_на_вещь на наличие в бд по полю \"id\" (результат: НЕ найден)")
    @Test
    void idIsExists_whenIdNotFound_thenExceptionIsReturned() {
        when(itemRequestRepository.existsById(anyLong())).thenThrow(ItemRequestIdNotFound.class);

        Assertions.assertThrows(ItemRequestIdNotFound.class, () -> itemRequestServiceImpl.idIsExists(anyLong()));
    }
}
