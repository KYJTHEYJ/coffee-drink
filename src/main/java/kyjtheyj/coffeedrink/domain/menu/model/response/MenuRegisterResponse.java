package kyjtheyj.coffeedrink.domain.menu.model.response;

import kyjtheyj.coffeedrink.domain.menu.entity.MenuEntity;
import kyjtheyj.coffeedrink.domain.menu.entity.MenuStockEntity;

import java.math.BigInteger;
import java.util.UUID;

public record MenuRegisterResponse(
        UUID menuId
        , String name
        , BigInteger price
        , String description
        , int sortNumber
        , BigInteger quantity
) {
    public static MenuRegisterResponse register(MenuEntity menu, MenuStockEntity menuStock) {
        return new MenuRegisterResponse(
                menu.getId()
                , menu.getName()
                , menu.getPrice()
                , menu.getDescription()
                , menu.getSortNumber()
                , menuStock.getQuantity()
        );
    }
}
