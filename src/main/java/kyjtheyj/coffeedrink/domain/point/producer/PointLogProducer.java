package kyjtheyj.coffeedrink.domain.point.producer;

import kyjtheyj.coffeedrink.common.model.kafka.event.PointLogEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import static kyjtheyj.coffeedrink.common.constant.KafkaConst.POINT_CHARGE_LOG_TOPIC;
import static kyjtheyj.coffeedrink.common.constant.KafkaConst.POINT_USE_LOG_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointLogProducer {
    private final KafkaTemplate<String, String> stringKafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendAddPointLog(PointLogEvent event) {
        stringKafkaTemplate.send(POINT_CHARGE_LOG_TOPIC, event.memberId().toString(), objectMapper.writeValueAsString(event));
    }

    public void sendUsePointLog(PointLogEvent event) {
        stringKafkaTemplate.send(POINT_USE_LOG_TOPIC, event.memberId().toString(), objectMapper.writeValueAsString(event));
    }
}
