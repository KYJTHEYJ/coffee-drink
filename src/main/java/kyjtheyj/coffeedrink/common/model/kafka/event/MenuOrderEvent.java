package kyjtheyj.coffeedrink.common.model.kafka.event;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record MenuOrderEvent(
        List<UUID> menuIdList
        , LocalDate orderDt
) {}
