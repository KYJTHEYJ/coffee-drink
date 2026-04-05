package kyjtheyj.coffeedrink.domain.point.event;

import kyjtheyj.coffeedrink.common.model.kafka.event.PointLogEvent;
import kyjtheyj.coffeedrink.domain.point.entity.PointLogType;
import kyjtheyj.coffeedrink.domain.point.producer.PointLogProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PointEventHandler {
    private final PointLogProducer pointLogProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PointLogEvent event) {
        if(event.type() == PointLogType.CHARGE) {
            pointLogProducer.sendAddPointLog(event);
        } else if(event.type() == PointLogType.USE) {
            pointLogProducer.sendUsePointLog(event);
        }
    }
}
