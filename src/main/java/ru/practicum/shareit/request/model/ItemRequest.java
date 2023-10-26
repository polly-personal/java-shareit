package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "requests", schema = "public")
@Entity
public class ItemRequest {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor;

}
