package kyjtheyj.coffeedrink.domain.order.controller;

import kyjtheyj.coffeedrink.common.model.principal.MemberPrincipal;
import kyjtheyj.coffeedrink.common.model.response.BaseResponse;
import kyjtheyj.coffeedrink.domain.order.model.request.OrderMenuRequest;
import kyjtheyj.coffeedrink.domain.order.model.response.OrderMenuResponse;
import kyjtheyj.coffeedrink.domain.order.facade.OrderFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderFacade orderFacade;

    @PostMapping
    public ResponseEntity<BaseResponse<OrderMenuResponse>> orderMenu(
            @RequestBody OrderMenuRequest request
            , @AuthenticationPrincipal MemberPrincipal memberInfo
    ) {
        if (!memberInfo.memberId().equals(request.memberId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BaseResponse.fail(HttpStatus.FORBIDDEN.name(), "본인 계정만 주문 할 수 있습니다"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success(HttpStatus.CREATED.name(), "주문 성공", orderFacade.orderMenu(memberInfo.memberId(), request)));
    }

}
