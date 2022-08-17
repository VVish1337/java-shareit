package ru.practicum.shareit.item.model;

import lombok.*;

/**
 * // TODO .
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long owner;
}
