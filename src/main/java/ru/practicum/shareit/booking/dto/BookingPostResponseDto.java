package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingPostResponseDto {
    private Long id;
    private Item item;
    private LocalDateTime start;
    private LocalDateTime end;
}