package kyjtheyj.coffeedrink.domain.order.repository;

import kyjtheyj.coffeedrink.domain.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}
