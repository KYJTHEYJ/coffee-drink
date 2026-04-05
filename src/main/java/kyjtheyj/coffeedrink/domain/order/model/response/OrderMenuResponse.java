package kyjtheyj.coffeedrink.domain.order.model.response;

import java.math.BigInteger;
import java.util.UUID;

public record OrderMenuResponse(
    UUID orderId
    , String orderNo
    , BigInteger totalPrice
) {
}
