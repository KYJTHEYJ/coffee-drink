package kyjtheyj.coffeedrink.domain.menu.repository;

import kyjtheyj.coffeedrink.domain.menu.model.dto.MenuOrderCountDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MenuOrderCountQueryRepository {
    List<MenuOrderCountDto> selectTopMenus(LocalDate start, LocalDate end, int limit);
}
