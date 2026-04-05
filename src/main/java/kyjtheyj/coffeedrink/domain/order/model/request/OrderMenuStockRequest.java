package kyjtheyj.coffeedrink.domain.order.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigInteger;
import java.util.UUID;

public record OrderMenuStockRequest(
        @NotNull(message = "메뉴 식별자는 필수 입니다")
        UUID menuId

        , @NotNull(message = "수량은 필수입니다")
        @Min(value = 1, message = "수량은 1개 이상이어야 합니다")
        BigInteger quantity
) {
}
