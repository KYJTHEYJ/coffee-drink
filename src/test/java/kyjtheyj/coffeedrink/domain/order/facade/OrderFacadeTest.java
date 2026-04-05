package kyjtheyj.coffeedrink.domain.order.facade;

import kyjtheyj.coffeedrink.common.exception.ServiceErrorException;
import kyjtheyj.coffeedrink.domain.member.fixture.MemberFixture;
import kyjtheyj.coffeedrink.domain.menu.repository.MenuRepository;
import kyjtheyj.coffeedrink.domain.menu.service.MenuService;
import kyjtheyj.coffeedrink.domain.order.fixture.OrderFixture;
import kyjtheyj.coffeedrink.domain.order.model.response.OrderMenuResponse;
import kyjtheyj.coffeedrink.domain.order.service.OrderService;
import kyjtheyj.coffeedrink.domain.point.repository.PointRepository;
import kyjtheyj.coffeedrink.domain.point.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static kyjtheyj.coffeedrink.common.exception.domain.MenuExceptionEnum.ERR_MENU_NOT_FOUND;
import static kyjtheyj.coffeedrink.common.exception.domain.MenuExceptionEnum.ERR_MENU_QUANTITY_LESS;
import static kyjtheyj.coffeedrink.common.exception.domain.PointExceptionEnum.ERR_POINT_NOT_ENOUGH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderFacadeTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    PointRepository pointRepository;

    @Mock
    MenuService menuService;

    @Mock
    PointService pointService;

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderFacade orderFacade;

    @Test
    @DisplayName("주문 생성 성공")
    void orderMenu() {
        given(menuRepository.findById(OrderFixture.menuId)).willReturn(Optional.of(OrderFixture.menuEntity()));
        given(pointRepository.findByMemberId(MemberFixture.memberId)).willReturn(Optional.of(OrderFixture.pointEntityWithEnoughBalance()));
        given(orderService.saveOrder(any(), any(), any())).willReturn(OrderFixture.orderMenuResponse());

        OrderMenuResponse response = orderFacade.orderMenu(MemberFixture.memberId, OrderFixture.orderMenuRequest());

        assertThat(response.totalPrice()).isEqualTo(OrderFixture.totalPrice);
        verify(menuService).decreaseStock(OrderFixture.menuId, OrderFixture.orderQuantity);
        verify(pointService).usePoint(MemberFixture.memberId, OrderFixture.totalPrice);
        verify(orderService).saveOrder(any(), any(), any());
    }

    @Test
    @DisplayName("주문 생성 실패 - 메뉴 없음")
    void orderMenu_fail_menuNotFound() {
        given(menuRepository.findById(OrderFixture.menuId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderFacade.orderMenu(MemberFixture.memberId, OrderFixture.orderMenuRequest()))
                .isInstanceOf(ServiceErrorException.class)
                .hasMessageContaining(ERR_MENU_NOT_FOUND.getMessage());

        verify(pointService, never()).usePoint(any(), any());
        verify(orderService, never()).saveOrder(any(), any(), any());
    }

    @Test
    @DisplayName("주문 생성 실패 - 포인트 잔액 부족")
    void orderMenu_fail_pointNotEnough() {
        given(menuRepository.findById(OrderFixture.menuId)).willReturn(Optional.of(OrderFixture.menuEntity()));
        given(pointRepository.findByMemberId(MemberFixture.memberId)).willReturn(Optional.of(OrderFixture.pointEntityWithNotEnoughBalance()));

        assertThatThrownBy(() -> orderFacade.orderMenu(MemberFixture.memberId, OrderFixture.orderMenuRequest()))
                .isInstanceOf(ServiceErrorException.class)
                .hasMessageContaining(ERR_POINT_NOT_ENOUGH.getMessage());

        verify(menuService, never()).decreaseStock(any(), any());
        verify(orderService, never()).saveOrder(any(), any(), any());
    }

    @Test
    @DisplayName("주문 생성 실패 - 재고 부족")
    void orderMenu_fail_decreaseStock() {
        given(menuRepository.findById(OrderFixture.menuId)).willReturn(Optional.of(OrderFixture.menuEntity()));
        given(pointRepository.findByMemberId(MemberFixture.memberId)).willReturn(Optional.of(OrderFixture.pointEntityWithEnoughBalance()));
        doThrow(new ServiceErrorException(ERR_MENU_QUANTITY_LESS)).when(menuService).decreaseStock(any(), any());

        // 재고 차감 자체가 실패했으므로 decreasedStocks 가 비어있어 복구 호출 없음
        assertThatThrownBy(() -> orderFacade.orderMenu(MemberFixture.memberId, OrderFixture.orderMenuRequest()))
                .isInstanceOf(ServiceErrorException.class);

        verify(pointService, never()).usePoint(any(), any());
        verify(menuService, never()).increaseStock(any(), any());
        verify(orderService, never()).saveOrder(any(), any(), any());
    }

    @Test
    @DisplayName("주문 생성 실패 - 포인트 차감 실패")
    void orderMenu_fail_usePoint() {
        given(menuRepository.findById(OrderFixture.menuId)).willReturn(Optional.of(OrderFixture.menuEntity()));
        given(pointRepository.findByMemberId(MemberFixture.memberId)).willReturn(Optional.of(OrderFixture.pointEntityWithEnoughBalance()));
        doThrow(new ServiceErrorException(ERR_POINT_NOT_ENOUGH)).when(pointService).usePoint(any(), any());

        assertThatThrownBy(() -> orderFacade.orderMenu(MemberFixture.memberId, OrderFixture.orderMenuRequest()))
                .isInstanceOf(ServiceErrorException.class);

        // 재고 차감은 성공했으므로 보상으로 재고 복구 호출
        verify(menuService).increaseStock(OrderFixture.menuId, OrderFixture.orderQuantity);
        verify(orderService, never()).saveOrder(any(), any(), any());
    }

    @Test
    @DisplayName("주문 생성 실패 - 주문 저장 실패")
    void orderMenu_fail_saveOrder() {
        given(menuRepository.findById(OrderFixture.menuId)).willReturn(Optional.of(OrderFixture.menuEntity()));
        given(pointRepository.findByMemberId(MemberFixture.memberId)).willReturn(Optional.of(OrderFixture.pointEntityWithEnoughBalance()));
        given(orderService.saveOrder(any(), any(), any())).willThrow(new RuntimeException("ERROR"));

        assertThatThrownBy(() -> orderFacade.orderMenu(MemberFixture.memberId, OrderFixture.orderMenuRequest()))
                .isInstanceOf(RuntimeException.class);

        // 포인트 차감, 재고 차감 모두 성공했으므로 둘 다 보상
        verify(pointService).recoveryPoint(MemberFixture.memberId, OrderFixture.totalPrice);
        verify(menuService).increaseStock(OrderFixture.menuId, OrderFixture.orderQuantity);
    }
}
