package kyjtheyj.coffeedrink.domain.menu.repository;

import kyjtheyj.coffeedrink.domain.menu.entity.MenuOrderCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface MenuOrderCountRepository extends JpaRepository<MenuOrderCountEntity, UUID> {
    Optional<MenuOrderCountEntity> findByMenuIdAndCountDt(UUID menuId, LocalDate countDt);
}
