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
@Table(name = "Point", uniqueConstraints = {
        @UniqueConstraint(name = "uk_point_id", columnNames = {"id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointEntity extends BaseTimeWithDelEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "member_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID memberId;

    @Column(nullable = false)
    private BigInteger balance;

    public static PointEntity register(UUID memberId) {
        PointEntity point = new PointEntity();
        point.memberId = memberId;
        point.balance = BigInteger.ZERO;
        return point;
    }

    public void increasePoint(BigInteger point) {
        this.balance = this.balance.add(point);
    }

    public void decreasePoint(BigInteger point) {
        this.balance = this.balance.subtract(point);
    }
}
