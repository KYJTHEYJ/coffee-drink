package kyjtheyj.coffeedrink.domain.member.controller;

import jakarta.validation.Valid;
import kyjtheyj.coffeedrink.common.model.BaseResponse;
import kyjtheyj.coffeedrink.domain.member.model.request.MemberRegisterRequest;
import kyjtheyj.coffeedrink.domain.member.model.response.MemberRegisterResponse;
import kyjtheyj.coffeedrink.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 로그인
    // 비밀번호 변경
    // 탈퇴

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<MemberRegisterResponse>> signUp(@Valid @RequestBody MemberRegisterRequest request) {
        MemberRegisterResponse response = memberService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(HttpStatus.CREATED.name(), "회원가입이 완료되었습니다", response));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<MemberRegisterResponse>> signUp(@Valid @RequestBody MemberRegisterRequest request) {
        MemberRegisterResponse response = memberService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(HttpStatus.CREATED.name(), "회원가입이 완료되었습니다", response));
    }


}
