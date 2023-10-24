package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "items", schema = "public")
@Entity
public class Item {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 512)
    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean available;

    @JoinColumn(name = "owner_id")
    @ManyToOne(fetch = FetchType.LAZY)
//    private Long ownerId;
    private User owner;

    @JoinColumn(name = "request_id")
    @OneToOne
//    private Long requestId;
    private ItemRequest itemRequest; // если вещь была создана по запросу, то это ссылка на запрос


//    public Item(Long id, String name, String description, Boolean isAvailable, /*Long requestId*/ ItemRequest itemRequest) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.isAvailable = isAvailable;
////        this.requestId = requestId;
//        this.itemRequest = itemRequest;
//    }
}
