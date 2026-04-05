package kyjtheyj.coffeedrink.domain.menu.controller;

import jakarta.validation.Valid;
import kyjtheyj.coffeedrink.common.model.BaseResponse;
import kyjtheyj.coffeedrink.domain.menu.model.request.MenuRegisterRequest;
import kyjtheyj.coffeedrink.domain.menu.model.response.MenuRegisterResponse;
import kyjtheyj.coffeedrink.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<MenuRegisterResponse> register(@Valid @RequestBody MenuRegisterRequest request) {
        MenuRegisterResponse response = menuService.register(request);
        return BaseResponse.success(HttpStatus.CREATED.name(), "메뉴가 등록되었습니다", response);
    }
}
