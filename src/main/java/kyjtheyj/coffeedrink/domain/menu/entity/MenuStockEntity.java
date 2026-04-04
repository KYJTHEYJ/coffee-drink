package kyjtheyj.coffeedrink.domain.menu.entity;

import jakarta.persistence.*;
import kyjtheyj.coffeedrink.common.entity.BaseTimeWithDelEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
@Table(name = "menu_stocks", uniqueConstraints = {
        @UniqueConstraint(name = "uk_menu_stocks_id", columnNames = {"id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuStockEntity extends BaseTimeWithDelEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "menu_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID menuId;

    @Column(nullable = false)
    private long quantity;

    public static MenuStockEntity register(UUID menuId, long quantity) {
        MenuStockEntity entity = new MenuStockEntity();
        entity.menuId = menuId;
        entity.quantity = quantity;
        return entity;
    }

    public void updateQuantity(long quantity) {
        this.quantity = quantity;
    }
}
