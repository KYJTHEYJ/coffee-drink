package kyjtheyj.coffeedrink.domain.order.service;

import kyjtheyj.coffeedrink.common.model.kafka.event.MenuOrderEvent;
import kyjtheyj.coffeedrink.common.model.kafka.event.OrderDataCollectionEvent;
import kyjtheyj.coffeedrink.common.model.kafka.event.PointLogEvent;
import kyjtheyj.coffeedrink.domain.menu.entity.MenuEntity;
import kyjtheyj.coffeedrink.domain.menu.entity.MenuOrderCountEntity;
import kyjtheyj.coffeedrink.domain.menu.repository.MenuOrderCountRepository;
import kyjtheyj.coffeedrink.domain.menu.repository.MenuRepository;
import kyjtheyj.coffeedrink.domain.order.entity.OrderEntity;
import kyjtheyj.coffeedrink.domain.order.entity.OrderMenuEntity;
import kyjtheyj.coffeedrink.domain.order.model.request.OrderMenuStockRequest;
import kyjtheyj.coffeedrink.domain.order.model.response.OrderMenuResponse;
import kyjtheyj.coffeedrink.domain.order.repository.OrderMenuRepository;
import kyjtheyj.coffeedrink.domain.order.repository.OrderRepository;
import kyjtheyj.coffeedrink.domain.point.entity.PointLogType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final MenuOrderCountRepository menuOrderCountRepository;
    private final ApplicationEventPublisher eventPublisher;

    // 주문 저장부
    @Transactional
    public OrderMenuResponse saveOrder(
            UUID memberId
            , BigInteger totalPrice
            , List<OrderMenuStockRequest> orderMenuStockRequestList
    ) {
        // 주문 저장
        OrderEntity order = OrderEntity.register(memberId, UUID.randomUUID().toString().replace("-", ""), totalPrice, LocalDateTime.now());
        orderRepository.save(order);

        // 주문한 메뉴 리스트
        List<UUID> menuIdList = orderMenuStockRequestList.stream().map(orderMenuStockRequest -> orderMenuStockRequest.menuId()).toList();
        // 주문 메뉴 리스트를 기반으로 한 메뉴 아이디와 메뉴 정보 Map
        Map<UUID, MenuEntity> menuEntityMap = menuRepository.findAllById(menuIdList).stream().collect(Collectors.toMap(menuEntity -> menuEntity.getId(), menuEntity -> menuEntity));

        // 주문 메뉴 저장
        List<OrderMenuEntity> orderMenuEntityList = orderMenuStockRequestList.stream()
                .map(orderMenuStockRequest -> {
                    MenuEntity menu = menuEntityMap.get(orderMenuStockRequest.menuId());
                    return OrderMenuEntity.register(
                            order.getId()
                            , orderMenuStockRequest.menuId()
                            , menu.getName()
                            , menu.getPrice()
                            , orderMenuStockRequest.quantity()
                            , menu.getPrice().multiply(orderMenuStockRequest.quantity())
                    );
                }).toList();
        orderMenuRepository.saveAll(orderMenuEntityList);

        // 주문 횟수 저장
        orderMenuStockRequestList.forEach(orderMenuStockRequest -> menuOrderCountRepository.findByMenuIdAndCountDt(orderMenuStockRequest.menuId(), order.getOrderAt().toLocalDate())
                .ifPresentOrElse(menuOrderCountEntity -> menuOrderCountEntity.increaseOrderCount(1L)
                        , () -> menuOrderCountRepository.save(MenuOrderCountEntity.register(orderMenuStockRequest.menuId(), 1L, order.getOrderAt().toLocalDate()))
                )
        );

        // 커밋 후에 전달 될수 있도록 추가
        eventPublisher.publishEvent(new PointLogEvent(memberId, order.getId(), totalPrice, PointLogType.USE));
        eventPublisher.publishEvent(new MenuOrderEvent(menuIdList, LocalDate.now()));
        eventPublisher.publishEvent(new OrderDataCollectionEvent(memberId, menuIdList, totalPrice));

        return new OrderMenuResponse(order.getId(), order.getOrderNo(), order.getTotalPrice());
    }
}
