package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("ItemRepositoryTest должен ")
@DataJpaTest
public class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    User user = User.builder().name("test_name_1").email("test_email_1").build();
    User requester = User.builder().name("test_name_2").email("test_email_2").build();

    ItemRequest itemRequest = ItemRequest.builder().description("test_description_1").created(LocalDateTime.now()).requester(requester).build();
    int from = 0;
    int size = 10;
    PageRequest pageRequest = PageRequest.of(from, size);


    @BeforeEach
    void saveUsers() {
        userRepository.save(user);
        userRepository.save(requester);
    }

    @DisplayName("сохранять запрос на вещь")
    @Test
    void save_whenSuccessInvoked_thenSavedItemRequestIsReturned() {
        Assertions.assertNull(itemRequest.getId());
        ItemRequest returnedItemRequest = itemRequestRepository.save(itemRequest);
        Assertions.assertEquals(itemRequest, returnedItemRequest);
    }

    @DisplayName("НЕ сохранять запрос на вещь, если поле \"description\" == null")
    @Test
    void save_whenDescriptionIsNull_thenSavedItemRequestIsNotReturned() {
        itemRequest.setDescription(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRequestRepository.save(itemRequest));
    }

    @DisplayName("НЕ сохранять запрос на вещь, если длина поля \"description\" > 512")
    @Test
    void save_whenDescriptionLengthIsMoreThen512_thenSavedItemRequestIsNotReturned() {
        String description = "test_description_1";
        itemRequest.setDescription(description.repeat(50));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRequestRepository.save(itemRequest));
    }

    @DisplayName("НЕ сохранять запрос на вещь, если поле \"created\" == null")
    @Test
    void save_whenCreatedIsNull_thenSavedItemRequestIsNotReturned() {
        itemRequest.setCreated(null);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRequestRepository.save(itemRequest));
    }

    @DisplayName("выдавать запрос на вещь по полю \"id\"")
    @Test
    void findById_whenInvoked_thenFoundItemRequestIsReturned() {
        itemRequestRepository.save(itemRequest);

        Optional<ItemRequest> returnedItemRequest = itemRequestRepository.findById(itemRequest.getId());

        Assertions.assertEquals(itemRequest, returnedItemRequest.get());
    }

    @DisplayName("выдавать все запросы_вещей владельца_запросов_вещей по полю \"requester.id\"")
    @Test
    void findAllByRequesterId_whenSuccessInvoked_thenFoundItemsIsReturned() {
        itemRequestRepository.save(itemRequest);

        List<ItemRequest> returnedItemRequests = itemRequestRepository.findAllByRequesterId(requester.getId());
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);

        Assertions.assertEquals(itemRequests, returnedItemRequests);
    }

    @DisplayName("выдавать для пользователя все запросы_вещей других_владельцов_запросов_вещей по полю \"requester.id\"")
    @Test
    void findAllWithoutRequester_whenSuccessInvoked_thenFoundItemsIsReturned() {
        itemRequestRepository.save(itemRequest);

        List<ItemRequest> returnedItemRequests = itemRequestRepository.findAllWithoutRequester(user.getId(), pageRequest);
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);

        Assertions.assertEquals(itemRequests, returnedItemRequests);
    }

    @DisplayName("сохранять запрос на вещь")
    @Test
    void existsById_whenSuccessInvoked_thenSavedItemRequestIsReturned() {
        itemRequestRepository.save(itemRequest);
        boolean result = itemRequestRepository.existsById(itemRequest.getId());
        Assertions.assertEquals(true, result);
    }
}
