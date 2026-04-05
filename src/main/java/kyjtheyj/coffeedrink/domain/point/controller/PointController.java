package kyjtheyj.coffeedrink.domain.point.controller;

import jakarta.validation.Valid;
import kyjtheyj.coffeedrink.common.model.response.BaseResponse;
import kyjtheyj.coffeedrink.common.model.principal.MemberPrincipal;
import kyjtheyj.coffeedrink.domain.point.model.request.PointChargeRequest;
import kyjtheyj.coffeedrink.domain.point.model.response.PointChargeResponse;
import kyjtheyj.coffeedrink.domain.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/points")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;

    @PostMapping
    public ResponseEntity<BaseResponse<PointChargeResponse>> chargePoint(
            @Valid @RequestBody PointChargeRequest request
            , @AuthenticationPrincipal MemberPrincipal memberInfo
    ) {
        if (!memberInfo.memberId().equals(request.memberId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BaseResponse.fail(HttpStatus.FORBIDDEN.name(), "본인 계정만 충전 할 수 있습니다"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(HttpStatus.OK.name(), "포인트 충전 성공", pointService.chargePoint(request.memberId(), request.addPoint())));
    }
}
