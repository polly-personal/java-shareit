package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ItemRepository extends JpaRepository<Item, Long> {
    //    Item create(Item item);
//
//    Item updateById(Long id, Item item);
//
//    Item deleteById(Long id);
//
//    Item getById(Long id);
//
//    List<Item> getAllByOwnerId(Long ownerId);
    Optional<List<Item>> findAllByOwnerId(Long ownerId);

    //
//    boolean idIsExists(Long id);
//
//    boolean itemIsExists(Item item);
//
//    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndIsAvailable(String nameSearch, String descriptionSearch, boolean available);

    List<Item> findAllByNameContainingIgnoreCase(String nameSearch);

    @Query("select i " +
            "from Item i " +
            "where i.name like concat ('%', ?1, '%')") // +Аккумуляторная/=/lower-аккумуляторная -аккУМУляторная/=/lower-аккумуляторная
//        @Query("select i " +
//            "from Item i " +
//            "where i.name like lower(concat ('%', ?1, '%'))") // +Аккумуляторная/=/lower-аккумуляторная -аккУМУляторная/=/lower-аккумуляторная
//        @Query("select i " +
//            "from Item i " +
//            "where (lower(i.name) like lower(concat ('%', ?1, '%')))") // +Аккумуляторная/=/lower-аккумуляторная -аккУМУляторная/=/lower-аккумуляторная
    List<Item> findAllBySearchText(String searchText);

    Set<Item> findAllByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableTrue(String text, String sameText);

//    Optional<Item> getOptionalOwnerByItemId(Long itemId);
}
