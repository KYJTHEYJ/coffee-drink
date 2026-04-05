package kyjtheyj.coffeedrink.domain.menu.producer;

import kyjtheyj.coffeedrink.common.model.kafka.event.MenuOrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import static kyjtheyj.coffeedrink.common.constant.KafkaConst.MENU_RANKING_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuRankingProducer {
    private final KafkaTemplate<String, String> stringKafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendMenuRanking(MenuOrderEvent event) {
        stringKafkaTemplate.send(MENU_RANKING_TOPIC, objectMapper.writeValueAsString(event));
    }
}
