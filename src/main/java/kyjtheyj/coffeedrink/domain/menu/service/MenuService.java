package kyjtheyj.coffeedrink.domain.menu.service;

import kyjtheyj.coffeedrink.common.model.PageResponse;
import kyjtheyj.coffeedrink.domain.menu.entity.MenuEntity;
import kyjtheyj.coffeedrink.domain.menu.entity.MenuStockEntity;
import kyjtheyj.coffeedrink.domain.menu.model.request.MenuRegisterRequest;
import kyjtheyj.coffeedrink.domain.menu.model.response.MenuListResponse;
import kyjtheyj.coffeedrink.domain.menu.model.response.MenuRegisterResponse;
import kyjtheyj.coffeedrink.domain.menu.repository.MenuRepository;
import kyjtheyj.coffeedrink.domain.menu.repository.MenuStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuStockRepository menuStockRepository;

    @CacheEvict(cacheNames = "menus",  allEntries = true)
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

    @Cacheable(cacheNames = "menus", key = "#page + ':' + #size")
    public PageResponse<MenuListResponse> menuList(int page, int size) {
        Page<MenuListResponse> menus = menuRepository
                .findAll(PageRequest.of(page, size, Sort.by("sortNumber").ascending()))
                .map(menuEntity ->
                        new MenuListResponse(
                                menuEntity.getId()
                                , menuEntity.getName()
                                , menuEntity.getPrice()
                                , menuEntity.getDescription()
                                , menuEntity.getSortNumber()
                        )
                );

        return PageResponse.register(menus);
    }
}
