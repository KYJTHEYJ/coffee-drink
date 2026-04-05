package kyjtheyj.coffeedrink.common.model.kafka.event;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public record OrderDataCollectionEvent(
        UUID memberId
        , List<UUID> menuIds
        , BigInteger totalPrice
) {}
