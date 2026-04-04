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
@Table(name = "menu_option_categories", uniqueConstraints = {
        @UniqueConstraint(name = "uk_menu_option_categories_id", columnNames = {"id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuOptionCategoryEntity extends BaseTimeWithDelEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "menu_id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID menuId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "sort_number", nullable = false)
    private int sortNumber;

    @Column(nullable = false)
    private boolean essential;

    public static MenuOptionCategoryEntity register(UUID menuId, String name, int sortNumber, boolean essential) {
        MenuOptionCategoryEntity entity = new MenuOptionCategoryEntity();
        entity.menuId = menuId;
        entity.name = name;
        entity.sortNumber = sortNumber;
        entity.essential = essential;
        return entity;
    }

    public void update(String name, int sortNumber, boolean essential) {
        this.name = name;
        this.sortNumber = sortNumber;
        this.essential = essential;
    }
}
