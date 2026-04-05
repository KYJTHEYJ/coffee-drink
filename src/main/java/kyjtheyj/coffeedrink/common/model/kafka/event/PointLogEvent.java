package kyjtheyj.coffeedrink.common.model.kafka.event;

import kyjtheyj.coffeedrink.domain.point.entity.PointLogType;

import java.math.BigInteger;
import java.util.UUID;

public record PointLogEvent(
        UUID memberId
        , UUID orderId
        , BigInteger amount
        , PointLogType type
) {
}
