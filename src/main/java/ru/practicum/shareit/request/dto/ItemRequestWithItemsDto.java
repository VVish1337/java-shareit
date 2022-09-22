package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemInRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestWithItemsDto {
    private long id;
    private String description;
    private LocalDateTime created;
    private List<ItemInRequestDto> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequestWithItemsDto that = (ItemRequestWithItemsDto) o;
        return id == that.id
                && Objects.equals(description, that.description)
                && Objects.equals(created, that.created)
                && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, created, items);
    }
}