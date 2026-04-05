package kyjtheyj.coffeedrink.domain.point.consumer;

import kyjtheyj.coffeedrink.common.exception.ServiceErrorException;
import kyjtheyj.coffeedrink.common.model.kafka.event.PointLogEvent;
import kyjtheyj.coffeedrink.domain.point.service.PointLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import static kyjtheyj.coffeedrink.common.constant.KafkaConst.POINT_CHARGE_LOG_TOPIC;
import static kyjtheyj.coffeedrink.common.constant.KafkaConst.POINT_USE_LOG_TOPIC;
import static kyjtheyj.coffeedrink.common.exception.domain.CommonExceptionEnum.ERR_GET_KAFKA_FAIL;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointLogConsumer {
    private final PointLogService pointLogService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = POINT_CHARGE_LOG_TOPIC
            , groupId = "coffee-drink-group"
            , containerFactory = "stringKafkaListenerContainerFactory"
            , concurrency = "3"
    )
    public void pointAddLogConsume(String message, Acknowledgment acknowledgment) {
        try {
            PointLogEvent pointLogEvent = objectMapper.readValue(message, PointLogEvent.class);
            pointLogService.save(pointLogEvent);
            acknowledgment.acknowledge();
        } catch (Exception e) {
           log.error("KAFKA 에러 발생 : {}", e.getMessage(), e);
           throw new ServiceErrorException(ERR_GET_KAFKA_FAIL);
        }
    }

    @KafkaListener(
            topics = POINT_USE_LOG_TOPIC
            , groupId = "coffee-drink-group"
            , containerFactory = "stringKafkaListenerContainerFactory"
            , concurrency = "3"
    )
    public void pointUseLogConsume(String message, Acknowledgment acknowledgment) {
        try {
            PointLogEvent pointLogEvent = objectMapper.readValue(message, PointLogEvent.class);
            pointLogService.save(pointLogEvent);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("KAFKA 에러 발생 : {}", e.getMessage(), e);
            throw new ServiceErrorException(ERR_GET_KAFKA_FAIL);
        }
    }
}
