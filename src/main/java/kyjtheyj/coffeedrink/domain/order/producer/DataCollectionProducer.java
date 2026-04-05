package kyjtheyj.coffeedrink.domain.order.producer;

import kyjtheyj.coffeedrink.common.model.kafka.event.OrderDataCollectionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import static kyjtheyj.coffeedrink.common.constant.KafkaConst.ORDER_DATA_COLLECTION_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataCollectionProducer {
    private final KafkaTemplate<String, String> stringKafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(OrderDataCollectionEvent event) {
        stringKafkaTemplate.send(
                ORDER_DATA_COLLECTION_TOPIC
                , event.memberId().toString()
                , objectMapper.writeValueAsString(event)
        );
    }
}
