package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "comments", schema = "public")
@Entity
public class Comment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = 512)
    private String text;

    @JoinColumn(name = "item_id")
    @ManyToOne
    private Item item;

    @JoinColumn(name = "author_id")
    @ManyToOne
    private User author;

    @Column
    private LocalDateTime created;
}