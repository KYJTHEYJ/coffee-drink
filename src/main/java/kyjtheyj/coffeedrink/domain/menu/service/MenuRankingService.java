package kyjtheyj.coffeedrink.domain.menu.service;

import kyjtheyj.coffeedrink.domain.menu.entity.MenuEntity;
import kyjtheyj.coffeedrink.domain.menu.model.dto.MenuOrderCountDto;
import kyjtheyj.coffeedrink.domain.menu.model.response.MenuRankResponse;
import kyjtheyj.coffeedrink.domain.menu.repository.MenuOrderCountQueryRepositoryImpl;
import kyjtheyj.coffeedrink.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static kyjtheyj.coffeedrink.common.constant.RedisConst.RANKING_KEY_PREFIX;
import static kyjtheyj.coffeedrink.common.constant.RedisConst.RANKING_RESULT_KEY_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuRankingService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final MenuOrderCountQueryRepositoryImpl menuOrderCountQueryRepository;
    private final MenuRepository menuRepository;

    public void increaseRank(UUID menuId, LocalDate orderDate) {
        String key = RANKING_KEY_PREFIX + orderDate;
        redisTemplate.opsForZSet().incrementScore(key, menuId, 1);
        redisTemplate.expire(key, 8, TimeUnit.DAYS);
    }

    // 최근 7일 인기 메뉴 TOP 3 조회
    public List<MenuRankResponse> getTop3In7DayPopularMenus() {
        List<String> keys = new ArrayList<>();

        for (int index = 0; index < 7; index++) {
            keys.add(RANKING_KEY_PREFIX + LocalDate.now().minusDays(index));
        }

        String resultKey = RANKING_RESULT_KEY_PREFIX + LocalDate.now();

        redisTemplate.opsForZSet().unionAndStore(keys.getFirst(), keys.subList(1, keys.size()), resultKey);
        redisTemplate.expire(resultKey, 1, TimeUnit.HOURS);

        Set<ZSetOperations.TypedTuple<Object>> top3Result = redisTemplate.opsForZSet().reverseRangeWithScores(resultKey, 0, 2);

        if (top3Result == null) {
            return getTop3In7DayMenusFromDB();
        }

        List<UUID> menuIds = top3Result.stream()
                .map(tuple -> UUID.fromString(Objects.requireNonNull(tuple.getValue()).toString()))
                .toList();

        Map<UUID, String> menuNameMap = menuRepository.findAllById(menuIds).stream()
                .collect(Collectors.toMap(menuEntity -> menuEntity.getId(), menuEntity -> menuEntity.getName()));

        return top3Result.stream()
                .map(tuple -> {
                    UUID menuId = UUID.fromString(Objects.requireNonNull(tuple.getValue()).toString());
                    return new MenuRankResponse(
                            menuId
                            , menuNameMap.get(menuId)
                            , tuple.getScore() != null ? tuple.getScore().longValue() : 0
                    );
                })
                .toList();
    }

    // Miss 시 조회
    private List<MenuRankResponse> getTop3In7DayMenusFromDB() {
        LocalDate startDate = LocalDate.now().minusDays(6);
        LocalDate endDate   = LocalDate.now();

        List<MenuOrderCountDto> results = menuOrderCountQueryRepository.selectTopMenus(startDate, endDate, 3);

        List<MenuEntity> menuEntityList = menuRepository.findAllById(results.stream()
                .map(menuOrderCountDto -> menuOrderCountDto.menuId()).toList());

        Map<UUID, String> menuNameMap = menuEntityList.stream()
                .collect(Collectors.toMap(menuEntity -> menuEntity.getId(), menuEntity -> menuEntity.getName()));

        return results.stream()
                .map(dto -> new MenuRankResponse(
                        dto.menuId()
                        , menuNameMap.get(dto.menuId())
                        , dto.totalCount()
                ))
                .toList();
    }
}
