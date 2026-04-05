package kyjtheyj.coffeedrink.domain.point.repository;

import kyjtheyj.coffeedrink.domain.point.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PointRepository extends JpaRepository<PointEntity, UUID> {
    Optional<PointEntity> findByMemberId(UUID memberId);
}
