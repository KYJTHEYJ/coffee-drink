package kyjtheyj.coffeedrink.domain.menu.consumer;

import kyjtheyj.coffeedrink.common.model.kafka.event.MenuOrderEvent;
import kyjtheyj.coffeedrink.domain.menu.service.MenuRankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import static kyjtheyj.coffeedrink.common.constant.KafkaConst.MENU_RANKING_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuRankingConsumer {
    private final MenuRankingService menuRankingService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = MENU_RANKING_TOPIC
            , groupId = "coffee-drink-group"
            , containerFactory = "stringKafkaListenerContainerFactory"
            , concurrency = "3"
    )
    public void consume(String message, Acknowledgment acknowledgment) {
        MenuOrderEvent event = objectMapper.readValue(message, MenuOrderEvent.class);
        event.menuIdList().forEach(menuId -> menuRankingService.increaseRank(menuId, event.orderDt()));
        acknowledgment.acknowledge();
    }
}
