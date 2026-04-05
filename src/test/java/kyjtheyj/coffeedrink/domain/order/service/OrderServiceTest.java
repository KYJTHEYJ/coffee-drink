package kyjtheyj.coffeedrink.domain.order.service;

import kyjtheyj.coffeedrink.common.model.kafka.event.PointLogEvent;
import kyjtheyj.coffeedrink.domain.member.fixture.MemberFixture;
import kyjtheyj.coffeedrink.domain.menu.entity.MenuEntity;
import kyjtheyj.coffeedrink.domain.menu.repository.MenuRepository;
import kyjtheyj.coffeedrink.domain.order.entity.OrderEntity;
import kyjtheyj.coffeedrink.domain.order.entity.OrderMenuEntity;
import kyjtheyj.coffeedrink.domain.order.fixture.OrderFixture;
import kyjtheyj.coffeedrink.domain.order.model.response.OrderMenuResponse;
import kyjtheyj.coffeedrink.domain.order.repository.OrderMenuRepository;
import kyjtheyj.coffeedrink.domain.order.repository.OrderRepository;
import kyjtheyj.coffeedrink.domain.point.entity.PointLogType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderMenuRepository orderMenuRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Mock
    MenuRepository menuRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    @DisplayName("주문 저장 성공 - 주문, 주문 메뉴 저장 및 USE 이벤트 발행")
    void saveOrder() {
        given(menuRepository.findAllById(any())).willReturn(List.of(OrderFixture.menuEntity()));

        OrderMenuResponse response = orderService.saveOrder(
                MemberFixture.memberId
                , OrderFixture.totalPrice
                , List.of(OrderFixture.orderMenuStockRequest())
        );

        assertThat(response.totalPrice()).isEqualTo(OrderFixture.totalPrice);
        verify(orderRepository).save(any(OrderEntity.class));
        verify(orderMenuRepository).saveAll(any(List.class));

        // 이벤트 발행 검증 - USE 타입, 올바른 금액
        ArgumentCaptor<PointLogEvent> captor = ArgumentCaptor.forClass(PointLogEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());

        PointLogEvent publishedEvent = captor.getValue();
        assertThat(publishedEvent.type()).isEqualTo(PointLogType.USE);
        assertThat(publishedEvent.amount()).isEqualTo(OrderFixture.totalPrice);
        assertThat(publishedEvent.memberId()).isEqualTo(MemberFixture.memberId);
    }
}
