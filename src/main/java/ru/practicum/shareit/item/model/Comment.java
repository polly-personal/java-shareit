package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    //    @NotBlank
    @Column(nullable = false, length = 512)
    private String text;

    //    @NotNull
    @JoinColumn(name = "item_id")
    @ManyToOne
    private Item item;

    @JoinColumn(name = "author_id"/*, referencedColumnName = "id"*/)
    @ManyToOne
    private User author;

    @Column
    private LocalDateTime created /*= LocalDateTime.now()*/;
}