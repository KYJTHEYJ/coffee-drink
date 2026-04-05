package kyjtheyj.coffeedrink.domain.order.fixture;

import kyjtheyj.coffeedrink.domain.member.fixture.MemberFixture;
import kyjtheyj.coffeedrink.domain.menu.entity.MenuEntity;
import kyjtheyj.coffeedrink.domain.order.model.request.OrderMenuRequest;
import kyjtheyj.coffeedrink.domain.order.model.request.OrderMenuStockRequest;
import kyjtheyj.coffeedrink.domain.order.model.response.OrderMenuResponse;
import kyjtheyj.coffeedrink.domain.point.entity.PointEntity;

import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public class OrderFixture {

    public static final UUID menuId = UUID.randomUUID();
    public static final UUID orderId = UUID.randomUUID();
    public static final BigInteger menuPrice = BigInteger.valueOf(4500);
    public static final BigInteger orderQuantity = BigInteger.ONE;
    public static final BigInteger totalPrice = menuPrice.multiply(orderQuantity);

    public static final BigInteger enoughBalance = BigInteger.valueOf(10000);
    public static final BigInteger notEnoughBalance = BigInteger.valueOf(1000);

    public static final String userToken = "orderUserToken";

    public static OrderMenuStockRequest orderMenuStockRequest() {
        return new OrderMenuStockRequest(menuId, orderQuantity);
    }

    public static OrderMenuRequest orderMenuRequest() {
        return new OrderMenuRequest(MemberFixture.memberId, List.of(orderMenuStockRequest()));
    }

    public static OrderMenuResponse orderMenuResponse() {
        return new OrderMenuResponse(orderId, "TEST_ORDER_NO", totalPrice);
    }

    public static MenuEntity menuEntity() {
        MenuEntity menu = MenuEntity.register("아메리카노", menuPrice, "기본 아메리카노", 1);
        ReflectionTestUtils.setField(menu, "id", menuId);
        return menu;
    }

    public static PointEntity pointEntityWithEnoughBalance() {
        PointEntity point = PointEntity.register(MemberFixture.memberId);
        point.increasePoint(enoughBalance);
        return point;
    }

    public static PointEntity pointEntityWithNotEnoughBalance() {
        PointEntity point = PointEntity.register(MemberFixture.memberId);
        point.increasePoint(notEnoughBalance);
        return point;
    }
}
