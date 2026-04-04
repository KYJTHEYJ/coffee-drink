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
@Table(name = "menu_categories", uniqueConstraints = {
        @UniqueConstraint(name = "uk_menu_categories_id", columnNames = {"id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuCategoryEntity extends BaseTimeWithDelEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "sort_number", nullable = false)
    private int sortNumber;

    public static MenuCategoryEntity register(String name, int sortNumber) {
        MenuCategoryEntity entity = new MenuCategoryEntity();
        entity.name = name;
        entity.sortNumber = sortNumber;
        return entity;
    }

    public void update(String name, int sortNumber) {
        this.name = name;
        this.sortNumber = sortNumber;
    }
}
