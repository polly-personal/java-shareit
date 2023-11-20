package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.booking.constant.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Table(name = "bookings", schema = "public")
@Entity
public class Booking {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    @JoinColumn(name = "item_id")
    @ManyToOne
    private Item item;

    @JoinColumn(name = "booker_id")
    @ManyToOne
    private User booker;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}
