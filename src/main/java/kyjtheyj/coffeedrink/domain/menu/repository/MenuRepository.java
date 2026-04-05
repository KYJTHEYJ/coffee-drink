package kyjtheyj.coffeedrink.domain.menu.repository;

import kyjtheyj.coffeedrink.domain.menu.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MenuRepository extends JpaRepository<MenuEntity, UUID> {
}
