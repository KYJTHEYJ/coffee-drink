package kyjtheyj.coffeedrink.domain.order.event;

import kyjtheyj.coffeedrink.common.model.kafka.event.OrderDataCollectionEvent;
import kyjtheyj.coffeedrink.domain.order.producer.DataCollectionProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DataCollectionEventHandler {

    private final DataCollectionProducer dataCollectionProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderDataCollectionEvent event) {
        dataCollectionProducer.send(event);
    }
}
