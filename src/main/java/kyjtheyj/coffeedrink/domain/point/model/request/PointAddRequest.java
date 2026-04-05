package kyjtheyj.coffeedrink.domain.point.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigInteger;
import java.util.UUID;

public record PointAddRequest(
        @NotNull(message = "식별자는 필수 입니다")
        UUID memberId

        , @NotNull(message = "포인트 충전 값은 필수 입니다")
        @Min(value = 1000, message = "포인트는 1000원 부터 충전 가능 합니다")
        BigInteger addPoint
) {
}
