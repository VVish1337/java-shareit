package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemInRequestDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long requestId;
}