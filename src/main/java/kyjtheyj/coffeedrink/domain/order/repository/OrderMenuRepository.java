package kyjtheyj.coffeedrink.domain.order.repository;

import kyjtheyj.coffeedrink.domain.order.entity.OrderMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderMenuRepository extends JpaRepository<OrderMenuEntity, UUID> {
}
