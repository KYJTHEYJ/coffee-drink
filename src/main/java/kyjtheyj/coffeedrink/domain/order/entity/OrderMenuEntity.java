package kyjtheyj.coffeedrink.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
@Table(name = "order_menus", uniqueConstraints = {
        @UniqueConstraint(name = "uk_order_menus_id", columnNames = {"id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenuEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "order_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID orderId;

    @Column(name = "menu_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID menuId;

    @Column(name = "name_snap", nullable = false, length = 100)
    private String nameSnap;

    @Column(name = "price_snap", nullable = false)
    private long priceSnap;

    @Column(nullable = false)
    private long quantity;

    @Column(name = "total_price", nullable = false)
    private long totalPrice;

    public static OrderMenuEntity register(UUID orderId, UUID menuId, String nameSnap, long priceSnap, long quantity, long totalPrice) {
        OrderMenuEntity entity = new OrderMenuEntity();
        entity.orderId = orderId;
        entity.menuId = menuId;
        entity.nameSnap = nameSnap;
        entity.priceSnap = priceSnap;
        entity.quantity = quantity;
        entity.totalPrice = totalPrice;
        return entity;
    }
}
