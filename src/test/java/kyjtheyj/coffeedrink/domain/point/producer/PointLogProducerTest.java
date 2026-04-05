package kyjtheyj.coffeedrink.domain.point.producer;

import kyjtheyj.coffeedrink.common.model.kafka.event.PointLogEvent;
import kyjtheyj.coffeedrink.domain.point.fixture.PointFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import tools.jackson.databind.ObjectMapper;

import static kyjtheyj.coffeedrink.common.constant.KafkaConst.POINT_CHARGE_LOG_TOPIC;
import static kyjtheyj.coffeedrink.common.constant.KafkaConst.POINT_USE_LOG_TOPIC;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PointLogProducerTest {

    @Mock
    KafkaTemplate<String, String> stringKafkaTemplate;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    PointLogProducer pointLogProducer;

    @Test
    @DisplayName("충전 로그 발행 성공 - 충전 토픽/키/메시지 검증")
    void sendAddPointLog() {
        PointLogEvent event = PointFixture.chargeEvent();
        String serialized = PointFixture.chargeJson();

        given(objectMapper.writeValueAsString(event)).willReturn(serialized);

        pointLogProducer.sendAddPointLog(event);

        verify(stringKafkaTemplate).send(
                eq(POINT_CHARGE_LOG_TOPIC)
                , eq(event.memberId().toString())
                , eq(serialized)
        );
    }

    @Test
    @DisplayName("사용 로그 발행 성공 - 사용 토픽/키/메시지 검증")
    void sendUsePointLog() {
        PointLogEvent event = PointFixture.useEvent();
        String serialized = PointFixture.useJson();

        given(objectMapper.writeValueAsString(event)).willReturn(serialized);

        pointLogProducer.sendUsePointLog(event);

        verify(stringKafkaTemplate).send(
                eq(POINT_USE_LOG_TOPIC)
                , eq(event.memberId().toString())
                , eq(serialized)
        );
    }

    @Test
    @DisplayName("충전 로그 발행 실패 - 직렬화 오류")
    void sendAddPointLog_fail_serialization() {
        PointLogEvent event = PointFixture.chargeEvent();

        given(objectMapper.writeValueAsString(event)).willThrow(new RuntimeException("직렬화 실패"));

        assertThatThrownBy(() -> pointLogProducer.sendAddPointLog(event))
                .isInstanceOf(RuntimeException.class);

        verify(stringKafkaTemplate, never()).send(anyString(), anyString(), anyString());
    }
}
