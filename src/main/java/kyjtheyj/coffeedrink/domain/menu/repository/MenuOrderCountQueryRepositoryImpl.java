package kyjtheyj.coffeedrink.domain.menu.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kyjtheyj.coffeedrink.domain.menu.model.dto.MenuOrderCountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static kyjtheyj.coffeedrink.domain.menu.entity.QMenuOrderCountEntity.menuOrderCountEntity;

@Repository
@RequiredArgsConstructor
public class MenuOrderCountQueryRepositoryImpl implements MenuOrderCountQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MenuOrderCountDto> selectTopMenus(LocalDate start, LocalDate end, int limit) {
        return jpaQueryFactory
                .select(Projections.constructor(MenuOrderCountDto.class
                        , menuOrderCountEntity.menuId
                        , menuOrderCountEntity.orderCount.sumLong()
                ))
                .from(menuOrderCountEntity)
                .where(menuOrderCountEntity.countDt.between(start, end))
                .groupBy(menuOrderCountEntity.menuId)
                .orderBy(menuOrderCountEntity.orderCount.sumLong().desc())
                .limit(limit)
                .fetch();
    }
}
