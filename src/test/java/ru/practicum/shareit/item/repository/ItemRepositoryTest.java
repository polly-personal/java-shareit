package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("ItemRepositoryTest должен ")
@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    User owner = User.builder().name("test_name_1").email("test_email_1").build();
    Item item = Item.builder().name("test_name_1").description("test_description_1").available(true).owner(owner).build();

    int from = 0;
    int size = 10;
    PageRequest pageRequest = PageRequest.of(from, size);

    @BeforeEach
    void saveUser() {
        userRepository.save(owner);
    }

    @DisplayName("сохранять предмет")
    @Test
    void save_whenSuccessInvoked_thenSavedItemIsReturned() {
        Assertions.assertNull(item.getId());

        Item returnedItem = itemRepository.save(item);
        Assertions.assertEquals(item, returnedItem);
    }

    @DisplayName("НЕ сохранять предмет, если поле \"name\" == null")
    @Test
    void save_whenNameIsNull_thenSavedItemIsNotReturned() {
        item.setName(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRepository.save(item));
    }

    @DisplayName("НЕ сохранять предмет, если поле \"description\" == null")
    @Test
    void save_whenDescriptionIsNull_thenSavedItemIsNotReturned() {
        item.setDescription(null);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRepository.save(item));
    }

    @DisplayName("НЕ сохранять предмет, если длина поля \"description\" > 512")
    @Test
    void save_whenDescriptionLengthIsMoreThen512_thenSavedItemIsNotReturned() {
        String description = "test_description_1";
        item.setDescription(description.repeat(50));
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRepository.save(item));
    }

    @DisplayName("НЕ сохранять предмет, если поле \"available\" == null")
    @Test
    void save_whenEmailIsNotUnique_thenSavedUserIsNotReturned() {
        item.setAvailable(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRepository.save(item));
    }

    @DisplayName("выдавать все предметы владельца по полю \"owner.id\"")
    @Test
    void findAllByOwnerId_whenSuccessInvoked_thenFoundItemsIsReturned() {
        itemRepository.save(item);

        Page<Item> returnedItems = itemRepository.findAllByOwnerId(owner.getId(), pageRequest);
        List<Item> items = new ArrayList<>();
        items.add(item);

        Assertions.assertEquals(items, returnedItems.toList());
    }

    @DisplayName("выдавать все предметы владельца по слову, содержащемуся в поле \"name\" или \"description\", " +
            "игнорируя регистр")
    @Test
    void findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue_whenSuccessInvoked_thenFoundItemsIsReturned() {
        item.setName("test_name_1 and 1A");
        itemRepository.save(item);
        Item itemOther = Item.builder().name("test_name_2").description("test_description_2 and 1a").available(true).owner(owner).build();
        itemRepository.save(itemOther);

        String searchText = "1a";
        Page<Item> returnedItems =
                itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(searchText, searchText, pageRequest);
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(itemOther);

        Assertions.assertEquals(items, returnedItems.toList());
    }
}
