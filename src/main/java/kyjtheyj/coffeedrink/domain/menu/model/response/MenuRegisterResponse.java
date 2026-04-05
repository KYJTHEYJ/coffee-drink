package kyjtheyj.coffeedrink.domain.menu.model.response;

import kyjtheyj.coffeedrink.domain.menu.entity.MenuEntity;
import kyjtheyj.coffeedrink.domain.menu.entity.MenuStockEntity;

import java.util.UUID;

public record MenuRegisterResponse(
        UUID menuId
        , String name
        , long price
        , String description
        , int sortNumber
        , long quantity
) {
    public static MenuRegisterResponse register(MenuEntity menu, MenuStockEntity stock) {
        return new MenuRegisterResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getDescription(),
                menu.getSortNumber(),
                stock.getQuantity()
        );
    }
}
