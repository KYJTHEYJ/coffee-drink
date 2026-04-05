package kyjtheyj.coffeedrink.domain.point.consumer;

import kyjtheyj.coffeedrink.common.exception.ServiceErrorException;
import kyjtheyj.coffeedrink.common.model.kafka.event.PointLogEvent;
import kyjtheyj.coffeedrink.domain.point.fixture.PointFixture;
import kyjtheyj.coffeedrink.domain.point.service.PointLogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PointLogConsumerTest {

    @Mock
    PointLogService pointLogService;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    Acknowledgment acknowledgment;

    @InjectMocks
    PointLogConsumer pointLogConsumer;

    @Test
    @DisplayName("충전 로그 수신 성공 - 저장 완료 후 수동 커밋")
    void pointAddLogConsume() {
        String message = PointFixture.chargeJson();
        PointLogEvent event = PointFixture.chargeEvent();

        given(objectMapper.readValue(eq(message), eq(PointLogEvent.class))).willReturn(event);

        pointLogConsumer.pointAddLogConsume(message, acknowledgment);

        verify(pointLogService).save(event);
        verify(acknowledgment).acknowledge();
    }

    @Test
    @DisplayName("충전 로그 수신 실패 - DB 저장 실패 시 수동 커밋 안 함")
    void pointAddLogConsume_fail_save() {
        String message = PointFixture.chargeJson();
        PointLogEvent event = PointFixture.chargeEvent();

        given(objectMapper.readValue(eq(message), eq(PointLogEvent.class))).willReturn(event);
        willThrow(new RuntimeException("DB 저장 실패")).given(pointLogService).save(any());

        assertThatThrownBy(() -> pointLogConsumer.pointAddLogConsume(message, acknowledgment))
                .isInstanceOf(ServiceErrorException.class)
                .hasMessageContaining("Kafka 메세지 Consume 실패하였습니다");

        verify(acknowledgment, never()).acknowledge();
    }

    @Test
    @DisplayName("충전 로그 수신 실패 - 역직렬화 오류 시 수동 커밋 안 함")
    void pointAddLogConsume_fail_deserialization() {
        String message = "error";

        given(objectMapper.readValue(eq(message), eq(PointLogEvent.class)))
                .willThrow(new RuntimeException("역직렬화 실패"));

        assertThatThrownBy(() -> pointLogConsumer.pointAddLogConsume(message, acknowledgment))
                .isInstanceOf(ServiceErrorException.class)
                .hasMessageContaining("Kafka 메세지 Consume 실패하였습니다");

        verify(pointLogService, never()).save(any());
        verify(acknowledgment, never()).acknowledge();
    }

    @Test
    @DisplayName("사용 로그 수신 성공 - 저장 완료 후 수동 커밋")
    void pointUseLogConsume_success() {
        String message = PointFixture.useJson();
        PointLogEvent event = PointFixture.useEvent();

        given(objectMapper.readValue(eq(message), eq(PointLogEvent.class))).willReturn(event);

        pointLogConsumer.pointUseLogConsume(message, acknowledgment);

        verify(pointLogService).save(event);
        verify(acknowledgment).acknowledge();
    }

    @Test
    @DisplayName("사용 로그 수신 실패 - DB 저장 실패 시 수동 커밋 안 함")
    void pointUseLogConsume_fail_save() {
        String message = PointFixture.useJson();
        PointLogEvent event = PointFixture.useEvent();

        given(objectMapper.readValue(eq(message), eq(PointLogEvent.class))).willReturn(event);
        willThrow(new RuntimeException("DB 저장 실패")).given(pointLogService).save(any());

        assertThatThrownBy(() -> pointLogConsumer.pointUseLogConsume(message, acknowledgment))
                .isInstanceOf(ServiceErrorException.class)
                .hasMessageContaining("Kafka 메세지 Consume 실패하였습니다");

        verify(acknowledgment, never()).acknowledge();
    }

    @Test
    @DisplayName("충전 로그 수신 실패 - 역직렬화 오류 시 수동 커밋 안 함")
    void pointUseLogConsume_fail_deserialization() {
        String message = "error";

        given(objectMapper.readValue(eq(message), eq(PointLogEvent.class)))
                .willThrow(new RuntimeException("역직렬화 실패"));

        assertThatThrownBy(() -> pointLogConsumer.pointUseLogConsume(message, acknowledgment))
                .isInstanceOf(ServiceErrorException.class)
                .hasMessageContaining("Kafka 메세지 Consume 실패하였습니다");

        verify(pointLogService, never()).save(any());
        verify(acknowledgment, never()).acknowledge();
    }
}
