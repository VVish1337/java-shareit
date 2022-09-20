package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestPostDto {
    @NotNull
    @NotEmpty
    private String description;
}