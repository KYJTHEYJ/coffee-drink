package kyjtheyj.coffeedrink.domain.menu.service;

import kyjtheyj.coffeedrink.domain.menu.entity.MenuEntity;
import kyjtheyj.coffeedrink.domain.menu.entity.MenuStockEntity;
import kyjtheyj.coffeedrink.domain.menu.model.request.MenuRegisterRequest;
import kyjtheyj.coffeedrink.domain.menu.model.response.MenuRegisterResponse;
import kyjtheyj.coffeedrink.domain.menu.repository.MenuRepository;
import kyjtheyj.coffeedrink.domain.menu.repository.MenuStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuStockRepository menuStockRepository;

    @Transactional
    public MenuRegisterResponse register(MenuRegisterRequest request) {
        MenuEntity menu = MenuEntity.register(
                request.name(),
                request.price(),
                request.description(),
                request.sortNumber()
        );
        menuRepository.save(menu);

        MenuStockEntity stock = MenuStockEntity.register(menu.getId(), request.quantity());
        menuStockRepository.save(stock);

        return MenuRegisterResponse.register(menu, stock);
    }
}
