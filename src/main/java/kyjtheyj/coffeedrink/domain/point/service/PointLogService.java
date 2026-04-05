package kyjtheyj.coffeedrink.domain.point.service;

import kyjtheyj.coffeedrink.common.model.kafka.event.PointLogEvent;
import kyjtheyj.coffeedrink.domain.point.entity.PointLogEntity;
import kyjtheyj.coffeedrink.domain.point.repository.PointLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointLogService {
    private final PointLogRepository pointLogRepository;

    @Transactional
    public void save(PointLogEvent event) {
        PointLogEntity log = PointLogEntity.register(
                event.memberId()
                , event.orderId()
                , event.amount()
                , event.type()
        );

        pointLogRepository.save(log);
    }
}
