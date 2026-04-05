package kyjtheyj.coffeedrink.domain.point.repository;

import kyjtheyj.coffeedrink.domain.point.entity.PointLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PointLogRepository extends JpaRepository<PointLogEntity, UUID> {
}
