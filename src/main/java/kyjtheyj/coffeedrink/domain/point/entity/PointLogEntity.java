package kyjtheyj.coffeedrink.domain.point.entity;

import jakarta.persistence.*;
import kyjtheyj.coffeedrink.common.entity.BaseTimeWithDelEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigInteger;
import java.util.UUID;

@Getter
@Entity
@Table(name = "point_logs", uniqueConstraints = {
        @UniqueConstraint(name = "uk_point_logs_id", columnNames = {"id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointLogEntity extends BaseTimeWithDelEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "member_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID memberId;

    @Column(name = "order_id", updatable = false, columnDefinition = "BINARY(16)")
    private UUID orderId;

    @Column(nullable = false)
    private BigInteger amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointLogType type;

    public static PointLogEntity register(UUID memberId, UUID orderId, BigInteger amount, PointLogType type) {
        PointLogEntity entity = new PointLogEntity();
        entity.memberId = memberId;
        entity.orderId = orderId;
        entity.amount = amount;
        entity.type = type;
        return entity;
    }
}
