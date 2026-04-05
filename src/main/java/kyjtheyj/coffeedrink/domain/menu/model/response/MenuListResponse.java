package kyjtheyj.coffeedrink.domain.menu.model.response;

import java.math.BigInteger;
import java.util.UUID;

public record MenuListResponse(
        UUID menuId
        , String name
        , BigInteger price
        , String description
        , Integer sortNumber
) {
}
