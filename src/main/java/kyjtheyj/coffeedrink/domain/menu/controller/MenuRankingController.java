package kyjtheyj.coffeedrink.domain.menu.controller;

import kyjtheyj.coffeedrink.common.model.response.BaseResponse;
import kyjtheyj.coffeedrink.domain.menu.model.response.MenuRankResponse;
import kyjtheyj.coffeedrink.domain.menu.service.MenuRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/ranking")
@RequiredArgsConstructor
public class MenuRankingController {

    private final MenuRankingService menuRankingService;

    @GetMapping("/top3")
    public ResponseEntity<BaseResponse<List<MenuRankResponse>>> getRankingTop3In7Day() {
        List<MenuRankResponse> response = menuRankingService.getTop3In7DayPopularMenus();
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK.name(), "인기 메뉴 조회 성공", response));
    }
}
