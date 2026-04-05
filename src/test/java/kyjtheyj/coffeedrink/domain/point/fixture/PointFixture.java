package kyjtheyj.coffeedrink.domain.point.fixture;

import kyjtheyj.coffeedrink.common.model.kafka.event.PointLogEvent;
import kyjtheyj.coffeedrink.domain.member.fixture.MemberFixture;
import kyjtheyj.coffeedrink.domain.point.entity.PointEntity;
import kyjtheyj.coffeedrink.domain.point.entity.PointLogType;

import java.math.BigInteger;
import java.util.UUID;

public class PointFixture {
    public static final UUID memberId = MemberFixture.memberId;
    public static final UUID orderId = UUID.randomUUID();
    public static final BigInteger chargeAmount = BigInteger.valueOf(5000);
    public static final BigInteger useAmount = BigInteger.valueOf(3000);

    public static PointEntity pointEntity() {
        return PointEntity.register(memberId);
    }

    public static PointLogEvent chargeEvent() {
        return new PointLogEvent(memberId, null, chargeAmount, PointLogType.CHARGE);
    }

    public static PointLogEvent useEvent() {
        return new PointLogEvent(memberId, orderId, useAmount, PointLogType.USE);
    }

    // 포인트 충전 JSON 구문
    public static String chargeJson() {
        return String.format(
                "{\"memberId\":\"%s\",\"orderId\":null,\"amount\":5000,\"type\":\"CHARGE\"}",
                memberId
        );
    }

    // 포인트 사용 JSON 구문
    public static String useJson() {
        return String.format(
                "{\"memberId\":\"%s\",\"orderId\":\"%s\",\"amount\":3000,\"type\":\"USE\"}",
                memberId, orderId
        );
    }
}
