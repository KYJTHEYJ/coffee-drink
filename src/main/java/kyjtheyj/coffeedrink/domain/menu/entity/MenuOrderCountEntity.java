package kyjtheyj.coffeedrink.domain.menu.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "menu_order_count", uniqueConstraints = {
        @UniqueConstraint(name = "uk_menu_order_count_id", columnNames = {"id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuOrderCountEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "menu_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID menuId;

    @Column(name = "order_count", nullable = false)
    private long orderCount;

    @Column(name = "count_dt", nullable = false)
    private LocalDateTime countDt;

    public static MenuOrderCountEntity register(UUID menuId, long orderCount, LocalDateTime countDt) {
        MenuOrderCountEntity entity = new MenuOrderCountEntity();
        entity.menuId = menuId;
        entity.orderCount = orderCount;
        entity.countDt = countDt;
        return entity;
    }
}
