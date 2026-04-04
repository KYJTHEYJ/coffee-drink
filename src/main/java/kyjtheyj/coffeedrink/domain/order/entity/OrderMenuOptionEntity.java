package kyjtheyj.coffeedrink.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
@Table(name = "order_menu_options", uniqueConstraints = {
        @UniqueConstraint(name = "uk_order_menu_options_id", columnNames = {"id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenuOptionEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "order_menu_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID orderMenuId;

    @Column(name = "order_menu_option_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID orderMenuOptionId;

    @Column(name = "name_snap", nullable = false, length = 100)
    private String nameSnap;

    @Column(name = "price_snap", nullable = false)
    private long priceSnap;

    public static OrderMenuOptionEntity register(UUID orderMenuId, UUID orderMenuOptionId, String nameSnap, long priceSnap) {
        OrderMenuOptionEntity entity = new OrderMenuOptionEntity();
        entity.orderMenuId = orderMenuId;
        entity.orderMenuOptionId = orderMenuOptionId;
        entity.nameSnap = nameSnap;
        entity.priceSnap = priceSnap;
        return entity;
    }
}
