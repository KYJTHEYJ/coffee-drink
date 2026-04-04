package kyjtheyj.coffeedrink.domain.member.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kyjtheyj.coffeedrink.common.model.BaseResponse;
import kyjtheyj.coffeedrink.domain.member.model.request.MemberLoginRequest;
import kyjtheyj.coffeedrink.domain.member.model.request.MemberRefreshRequest;
import kyjtheyj.coffeedrink.domain.member.model.request.MemberRegisterRequest;
import kyjtheyj.coffeedrink.domain.member.model.response.MemberLoginResponse;
import kyjtheyj.coffeedrink.domain.member.model.response.MemberRefreshResponse;
import kyjtheyj.coffeedrink.domain.member.model.response.MemberRegisterResponse;
import kyjtheyj.coffeedrink.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<MemberRegisterResponse>> signUp(@Valid @RequestBody MemberRegisterRequest request) {
        MemberRegisterResponse response = memberService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(HttpStatus.CREATED.name(), "회원가입이 완료되었습니다", response));
    }

    @PostMapping("/signin")
    public ResponseEntity<BaseResponse<MemberLoginResponse>> signIn(@Valid @RequestBody MemberLoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.pwd()));
        MemberLoginResponse response = memberService.signIn(request);
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK.name(), "로그인이 완료되었습니다", response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse<MemberRefreshResponse>> refresh(@Valid @RequestBody MemberRefreshRequest request) {
        MemberRefreshResponse response = memberService.refresh(request);
        return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK.name(), "토큰이 재발급되었습니다", response));
    }


}
