package kyjtheyj.coffeedrink.domain.menu.event;

import kyjtheyj.coffeedrink.common.model.kafka.event.MenuOrderEvent;
import kyjtheyj.coffeedrink.domain.menu.producer.MenuRankingProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MenuOrderEventHandler {
    private final MenuRankingProducer menuRankingProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(MenuOrderEvent event) {
        menuRankingProducer.sendMenuRanking(event);
    }
}
