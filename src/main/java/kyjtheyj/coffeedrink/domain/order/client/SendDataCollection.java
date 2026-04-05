package kyjtheyj.coffeedrink.domain.order.client;

import kyjtheyj.coffeedrink.common.model.kafka.event.OrderDataCollectionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendDataCollection {

    private final RestClient restClient;

    public void send(OrderDataCollectionEvent event) {
        try {
            restClient.post()
                    .uri("https://localhost:8080/api/tests")
                    .body(event)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.info("Mock 전송, 주문한 회원의 UUID : {}", event.memberId());
        }
    }
}
