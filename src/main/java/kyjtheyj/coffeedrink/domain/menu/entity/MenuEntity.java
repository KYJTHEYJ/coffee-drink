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
@Table(name = "menus", uniqueConstraints = {
        @UniqueConstraint(name = "uk_menus_id", columnNames = {"id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuEntity extends BaseTimeWithDelEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private long price;

    @Column(name = "description", length = 350)
    private String description;

    @Column(name = "sort_number", nullable = false)
    private int sortNumber;

    public static MenuEntity register(UUID menuCategoryId, String name, long price, String descripton, int sortNumber) {
        MenuEntity entity = new MenuEntity();
        entity.name = name;
        entity.price = price;
        entity.description = descripton;
        entity.sortNumber = sortNumber;
        return entity;
    }

    public void update(String name, long price, String description, int sortNumber) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.sortNumber = sortNumber;
    }
}
