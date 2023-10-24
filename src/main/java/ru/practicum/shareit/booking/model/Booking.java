package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Data
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
//    private Long itemId;
    private Item item;

    @JoinColumn(name = "booker_id")
    @ManyToOne
//    private Long booker;
    private User booker;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Status status;

//    private Long ownerItem;

//    private CustomerReview customerReview; //todo что делать с этим полем?
}
