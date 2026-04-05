package kyjtheyj.coffeedrink.domain.order.model.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderMenuRequest(
        @NotNull(message = "회원 식별자는 필수 입니다")
        UUID memberId

        , @NotNull(message = "주문시 메뉴는 필수입니다")
        List<OrderMenuStockRequest> orderMenus
) {
}
