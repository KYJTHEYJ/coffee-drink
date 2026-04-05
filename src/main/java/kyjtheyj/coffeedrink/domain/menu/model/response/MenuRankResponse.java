package kyjtheyj.coffeedrink.domain.menu.model.response;

import java.util.UUID;

public record MenuRankResponse(
        UUID menuId
        , String name
        , Long count
) {
}
