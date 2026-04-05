package kyjtheyj.coffeedrink.domain.menu.repository;

import kyjtheyj.coffeedrink.domain.menu.entity.MenuStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MenuStockRepository extends JpaRepository<MenuStockEntity, UUID> {
    Optional<MenuStockEntity> findByMenuId(UUID menuId);
}
