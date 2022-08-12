package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * // TODO .
 */
@Getter
@Setter
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
}
