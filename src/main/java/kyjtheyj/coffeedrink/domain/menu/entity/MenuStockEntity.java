package kyjtheyj.coffeedrink.domain.menu.entity;

import jakarta.persistence.*;
import kyjtheyj.coffeedrink.common.entity.BaseTimeWithDelEntity;
import kyjtheyj.coffeedrink.common.exception.ServiceErrorException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigInteger;
import java.util.UUID;

import static kyjtheyj.coffeedrink.common.exception.domain.MenuExceptionEnum.ERR_MENU_QUANTITY_INVALID;
import static kyjtheyj.coffeedrink.common.exception.domain.MenuExceptionEnum.ERR_MENU_QUANTITY_LESS;

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
    private BigInteger quantity;

    public static MenuStockEntity register(UUID menuId, BigInteger quantity) {
        MenuStockEntity entity = new MenuStockEntity();
        entity.menuId = menuId;
        entity.quantity = quantity;
        return entity;
    }

    public void decreaseQuantity(BigInteger quantity) {
        if (quantity.compareTo(BigInteger.ZERO) <= 0) {
            throw new ServiceErrorException(ERR_MENU_QUANTITY_INVALID);
        }

        if (quantity.compareTo(this.quantity) > 0) {
            throw new ServiceErrorException(ERR_MENU_QUANTITY_LESS);
        }

        this.quantity = this.quantity.subtract(quantity);
    }

    public void increaseQuantity(BigInteger quantity) {
        if (quantity.compareTo(BigInteger.ZERO) <= 0) {
            throw new ServiceErrorException(ERR_MENU_QUANTITY_INVALID);
        }

        this.quantity = this.quantity.add(quantity);
    }
}
