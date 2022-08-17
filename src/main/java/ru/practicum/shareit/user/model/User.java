package ru.practicum.shareit.user.model;

import lombok.*;

/**
 * // TODO .
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private long id;
    private String name;
    private String email;
}