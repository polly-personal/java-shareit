package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;


/**
 * TODO Sprint add-controllers.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Table(name = "users", schema = "public")
@Entity
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 100, unique = true)
    private String email;
}
