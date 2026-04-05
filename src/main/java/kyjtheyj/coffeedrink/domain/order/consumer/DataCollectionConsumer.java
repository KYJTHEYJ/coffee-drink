package kyjtheyj.coffeedrink.domain.order.consumer;

import kyjtheyj.coffeedrink.common.model.kafka.event.OrderDataCollectionEvent;
import kyjtheyj.coffeedrink.domain.order.client.SendDataCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import static kyjtheyj.coffeedrink.common.constant.KafkaConst.ORDER_DATA_COLLECTION_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataCollectionConsumer {
    private final SendDataCollection SendDataCollection;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = ORDER_DATA_COLLECTION_TOPIC,
            groupId = "coffee-drink-group",
            containerFactory = "stringKafkaListenerContainerFactory"
    )
    public void consume(String message, Acknowledgment acknowledgment) {
        OrderDataCollectionEvent event = objectMapper.readValue(message, OrderDataCollectionEvent.class);
        SendDataCollection.send(event);
        acknowledgment.acknowledge();
    }
}
