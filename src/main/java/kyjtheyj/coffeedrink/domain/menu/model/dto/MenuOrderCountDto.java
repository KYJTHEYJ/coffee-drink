package kyjtheyj.coffeedrink.domain.menu.model.dto;

import java.util.UUID;

public record MenuOrderCountDto(
        UUID menuId
        , Long totalCount
) {
}
