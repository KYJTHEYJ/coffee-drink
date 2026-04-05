package kyjtheyj.coffeedrink.domain.menu.fixture;

import kyjtheyj.coffeedrink.domain.menu.model.request.MenuRegisterRequest;
import kyjtheyj.coffeedrink.domain.menu.model.response.MenuRegisterResponse;

import java.math.BigInteger;
import java.util.UUID;

public class MenuFixture {

    public static final UUID menuId = UUID.randomUUID();
    public static final String menuName = "아메리카노";
    public static final BigInteger menuPrice = BigInteger.valueOf(4500);
    public static final String menuDescription = "기본 아메리카노";
    public static final int menuSortNumber = 1;
    public static final long menuQuantity = 100L;

    public static final String adminToken = "adminToken";
    public static final String userToken = "userToken";
    public static final String adminEmail = "admin@admin.com";
    public static final String userEmail = "user@user.com";

    public static MenuRegisterRequest menuRegisterRequest() {
        return new MenuRegisterRequest(menuName, menuPrice, menuDescription, menuSortNumber, menuQuantity);
    }

    public static MenuRegisterResponse menuRegisterResponse() {
        return new MenuRegisterResponse(menuId, menuName, menuPrice, menuDescription, menuSortNumber, menuQuantity);
    }
}
