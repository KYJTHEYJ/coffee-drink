package kyjtheyj.coffeedrink.domain.point.event;

import kyjtheyj.coffeedrink.common.model.kafka.event.PointLogEvent;
import kyjtheyj.coffeedrink.domain.point.fixture.PointFixture;
import kyjtheyj.coffeedrink.domain.point.producer.PointLogProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PointEventHandlerTest {

    @Mock
    PointLogProducer pointLogProducer;

    @InjectMocks
    PointEventHandler pointEventHandler;

    @Test
    @DisplayName("CHARGE 수신")
    void handle_charge() {
        PointLogEvent event = PointFixture.chargeEvent();

        pointEventHandler.handle(event);

        verify(pointLogProducer).sendAddPointLog(event);
        verify(pointLogProducer, never()).sendUsePointLog(any());
    }

    @Test
    @DisplayName("USE 수신")
    void handle_use() {
        PointLogEvent event = PointFixture.useEvent();

        pointEventHandler.handle(event);

        verify(pointLogProducer).sendUsePointLog(event);
        verify(pointLogProducer, never()).sendAddPointLog(any());
    }
}
