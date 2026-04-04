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
@Table(name = "menu_options", uniqueConstraints = {
        @UniqueConstraint(name = "uk_menu_options_id", columnNames = {"id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuOptionEntity extends BaseTimeWithDelEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "menu_option_category_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID menuOptionCategoryId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private long price;

    @Column(name = "sort_number", nullable = false)
    private int sortNumber;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    public static MenuOptionEntity register(UUID menuOptionCategoryId, String name, long price, int sortNumber, boolean isDefault) {
        MenuOptionEntity entity = new MenuOptionEntity();
        entity.menuOptionCategoryId = menuOptionCategoryId;
        entity.name = name;
        entity.price = price;
        entity.sortNumber = sortNumber;
        entity.isDefault = isDefault;
        return entity;
    }

    public void update(String name, long price, int sortNumber, boolean isDefault) {
        this.name = name;
        this.price = price;
        this.sortNumber = sortNumber;
        this.isDefault = isDefault;
    }
}
