package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.item.marker.Create;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingPostDto {
    private Long id;
    private Long itemId;
    @FutureOrPresent(groups = {Create.class})
    private LocalDateTime start;
    @FutureOrPresent(groups = {Create.class})
    private LocalDateTime end;
}