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
    private User owner;

    @JoinColumn(name = "request_id")
    @OneToOne
    private ItemRequest itemRequest;
}
