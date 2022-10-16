package ru.practicum.shareit.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtil {
    public static Pageable getPageable(int from, int size, Sort sort) {
        int page = from / size;
        return PageRequest.of(page, size,sort);
    }
}





























