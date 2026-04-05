package kyjtheyj.coffeedrink.domain.order.facade;

import kyjtheyj.coffeedrink.common.exception.ServiceErrorException;
import kyjtheyj.coffeedrink.domain.menu.entity.MenuEntity;
import kyjtheyj.coffeedrink.domain.menu.repository.MenuRepository;
import kyjtheyj.coffeedrink.domain.menu.service.MenuService;
import kyjtheyj.coffeedrink.domain.order.model.request.OrderMenuRequest;
import kyjtheyj.coffeedrink.domain.order.model.request.OrderMenuStockRequest;
import kyjtheyj.coffeedrink.domain.order.model.response.OrderMenuResponse;
import kyjtheyj.coffeedrink.domain.order.service.OrderService;
import kyjtheyj.coffeedrink.domain.point.entity.PointEntity;
import kyjtheyj.coffeedrink.domain.point.repository.PointRepository;
import kyjtheyj.coffeedrink.domain.point.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static kyjtheyj.coffeedrink.common.exception.domain.MenuExceptionEnum.ERR_MENU_NOT_FOUND;
import static kyjtheyj.coffeedrink.common.exception.domain.PointExceptionEnum.ERR_POINT_NOT_ENOUGH;
import static kyjtheyj.coffeedrink.common.exception.domain.PointExceptionEnum.ERR_POINT_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderFacade {
    private final MenuRepository menuRepository;
    private final PointRepository pointRepository;

    private final MenuService menuService;
    private final PointService pointService;
    private final OrderService orderService;

    public OrderMenuResponse orderMenu(UUID memberId, OrderMenuRequest request) {

        // 메뉴 조회, 총 금액 계산부
        BigInteger totalPrice = BigInteger.ZERO;

        for (OrderMenuStockRequest orderMenuStockRequest : request.orderMenus()) {
            MenuEntity menu = menuRepository.findById(orderMenuStockRequest.menuId()).orElseThrow(() -> new ServiceErrorException(ERR_MENU_NOT_FOUND));
            totalPrice = totalPrice.add(menu.getPrice().multiply(orderMenuStockRequest.quantity()));
        }

        // 포인트 검증부
        PointEntity point = pointRepository.findByMemberId(memberId).orElseThrow(() -> new ServiceErrorException(ERR_POINT_NOT_FOUND));
        if (point.getBalance().compareTo(totalPrice) < 0) {
            throw new ServiceErrorException(ERR_POINT_NOT_ENOUGH);
        }

        // 재고 차감부, 실패시 재고 보상
        List<OrderMenuStockRequest> decreasedStocks = new ArrayList<>();
        try {
            for (OrderMenuStockRequest orderMenuStockRequest : request.orderMenus()) {
                menuService.decreaseStock(orderMenuStockRequest.menuId(), orderMenuStockRequest.quantity());
                decreasedStocks.add(orderMenuStockRequest);
            }
        } catch (Exception e) {
            for (OrderMenuStockRequest orderMenuStockRequest : decreasedStocks) {
                menuService.increaseStock(orderMenuStockRequest.menuId(), orderMenuStockRequest.quantity());
            }

            throw e;
        }

        // 포인트 차감부, 실패시 재고 보상
        try {
            pointService.usePoint(memberId, totalPrice);
        } catch (Exception e) {
            for (OrderMenuStockRequest orderMenuStockRequest : decreasedStocks) {
                menuService.increaseStock(orderMenuStockRequest.menuId(), orderMenuStockRequest.quantity());
            }

            throw e;
        }

        // 주문 저장, 실패시 재고 및 포인트까지 보상
        try {
            return orderService.saveOrder(memberId, totalPrice, request.orderMenus());
        } catch (Exception e) {
            pointService.recoveryPoint(memberId, totalPrice);
            for (OrderMenuStockRequest orderMenuStockRequest : decreasedStocks) {
                menuService.increaseStock(orderMenuStockRequest.menuId(), orderMenuStockRequest.quantity());
            }

            throw e;
        }
    }
}
