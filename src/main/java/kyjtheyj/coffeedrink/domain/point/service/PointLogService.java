package kyjtheyj.coffeedrink.domain.point.service;

import kyjtheyj.coffeedrink.domain.point.repository.PointLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointLogService {
    private final PointLogRepository pointLogRepository;
}
