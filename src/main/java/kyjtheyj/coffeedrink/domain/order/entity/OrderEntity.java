package kyjtheyj.coffeedrink.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "orders", uniqueConstraints = {
        @UniqueConstraint(name = "uk_orders_id", columnNames = {"id"})
        , @UniqueConstraint(name = "uk_orders_order_no", columnNames = {"order_no"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "member_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID memberId;

    @Column(name = "order_no", nullable = false, updatable = false, length = 100)
    private String orderNo;

    @Column(name = "total_price", nullable = false)
    private BigInteger totalPrice;

    @Column(name = "order_at", nullable = false)
    private LocalDateTime orderAt;

    public static OrderEntity register(UUID memberId, String orderNo, BigInteger totalPrice, LocalDateTime orderAt) {
        OrderEntity entity = new OrderEntity();
        entity.memberId = memberId;
        entity.orderNo = orderNo;
        entity.totalPrice = totalPrice;
        entity.orderAt = orderAt;
        return entity;
    }
}
