package kyjtheyj.coffeedrink.domain.menu.service;

import kyjtheyj.coffeedrink.common.model.response.PageResponse;
import kyjtheyj.coffeedrink.domain.menu.entity.MenuEntity;
import kyjtheyj.coffeedrink.domain.menu.entity.MenuStockEntity;
import kyjtheyj.coffeedrink.domain.menu.fixture.MenuFixture;
import kyjtheyj.coffeedrink.domain.menu.model.request.MenuRegisterRequest;
import kyjtheyj.coffeedrink.domain.menu.model.response.MenuListResponse;
import kyjtheyj.coffeedrink.domain.menu.model.response.MenuRegisterResponse;
import kyjtheyj.coffeedrink.domain.menu.repository.MenuRepository;
import kyjtheyj.coffeedrink.domain.menu.repository.MenuStockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuStockRepository menuStockRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 등록 성공")
    void register() {
        MenuRegisterRequest request = MenuFixture.menuRegisterRequest();

        MenuRegisterResponse response = menuService.register(request);

        assertThat(response.name()).isEqualTo(request.name());
        assertThat(response.price()).isEqualTo(request.price());
        assertThat(response.description()).isEqualTo(request.description());
        assertThat(response.sortNumber()).isEqualTo(request.sortNumber());
        assertThat(response.quantity()).isEqualTo(request.quantity());
        verify(menuRepository).save(any(MenuEntity.class));
        verify(menuStockRepository).save(any(MenuStockEntity.class));
    }

    @Test
    @DisplayName("메뉴 조회 성공")
    void menuList() {
        List<MenuEntity> menus = List.of(
                MenuEntity.register("아메리카노", BigInteger.valueOf(4500), "설명", 1)
        );

        PageRequest pageable = PageRequest.of(0, 10, Sort.by("sortNumber").ascending());
        Page<MenuEntity> menuPage = new PageImpl<>(menus, pageable, menus.size());

        given(menuRepository.findAll(any(Pageable.class))).willReturn(menuPage);

        PageResponse<MenuListResponse> response = menuService.menuList(0, 10);

        assertThat(response.content().getFirst().name()).isEqualTo("아메리카노");
        assertThat(response.currentPage()).isEqualTo(0);
        assertThat(response.totalElements()).isEqualTo(1);
        assertThat(response.isLast()).isTrue();
    }
}
