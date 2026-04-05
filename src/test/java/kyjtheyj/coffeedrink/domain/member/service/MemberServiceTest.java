package kyjtheyj.coffeedrink.domain.member.service;

import kyjtheyj.coffeedrink.common.config.jwt.JwtUtil;
import kyjtheyj.coffeedrink.common.exception.ServiceErrorException;
import kyjtheyj.coffeedrink.common.service.RedisService;
import kyjtheyj.coffeedrink.domain.member.model.request.MemberLoginRequest;
import kyjtheyj.coffeedrink.domain.member.model.request.MemberRefreshRequest;
import kyjtheyj.coffeedrink.domain.member.model.request.MemberRegisterRequest;
import kyjtheyj.coffeedrink.domain.member.model.response.MemberLoginResponse;
import kyjtheyj.coffeedrink.domain.member.model.response.MemberRefreshResponse;
import kyjtheyj.coffeedrink.domain.member.model.response.MemberRegisterResponse;
import kyjtheyj.coffeedrink.domain.member.fixture.MemberFixture;
import kyjtheyj.coffeedrink.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    MemberRepository memberRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtUtil jwtUtil;
    @Mock
    RedisService redisService;
    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("일반 회원가입 성공")
    void signUp() {
        MemberRegisterRequest request = MemberFixture.memberRegisterRequest();

        given(memberRepository.findMemberEntityByEmail(request.email())).willReturn(Optional.empty());
        given(passwordEncoder.encode(request.pwd())).willReturn(MemberFixture.memberEncodePassword);

        MemberRegisterResponse response = memberService.signUp(request);

        assertThat(response.email()).isEqualTo(request.email());
    }

    @Test
    @DisplayName("일반 회원가입 실패 - 이메일 중복")
    void signUp_fail_emailDup() {
        MemberRegisterRequest request = MemberFixture.memberRegisterRequest();

        given(memberRepository.findMemberEntityByEmail(request.email())).willReturn(Optional.of(MemberFixture.memberWithRoleUser()));

        assertThatThrownBy(() -> memberService.signUp(request))
                .isInstanceOf(ServiceErrorException.class)
                .hasMessageContaining("이미 사용 중인 이메일입니다");
    }

    @Test
    @DisplayName("로그인 성공시 토큰 발급")
    void signIn() {
        MemberLoginRequest request = MemberFixture.memberLoginRequest();
        given(jwtUtil.createAccessToken(request.email())).willReturn(MemberFixture.accessToken);
        given(jwtUtil.createRefreshToken(request.email())).willReturn(MemberFixture.refreshToken);

        MemberLoginResponse response = memberService.signIn(request);

        assertThat(response.accessToken()).isEqualTo(MemberFixture.accessToken);
        assertThat(response.refreshToken()).isEqualTo(MemberFixture.refreshToken);
        verify(redisService).saveRefreshToken(anyString(), anyString(), anyLong());
    }

    @Test
    @DisplayName("로그인 실패")
    void signIn_fail() {
        MemberLoginRequest request = MemberFixture.memberLoginRequest();
        given(jwtUtil.createAccessToken(request.email())).willReturn(MemberFixture.accessToken);
        given(jwtUtil.createRefreshToken(request.email())).willReturn(MemberFixture.refreshToken);

        MemberLoginResponse response = memberService.signIn(request);

        assertThat(response.accessToken()).isEqualTo(MemberFixture.accessToken);
        assertThat(response.refreshToken()).isEqualTo(MemberFixture.refreshToken);
        verify(redisService).saveRefreshToken(anyString(), anyString(), anyLong());
    }

    @Test
    @DisplayName("RefreshToken 으로 토큰 재발급 성공")
    void refreshToken_RTR() {
        MemberRefreshRequest request = MemberFixture.memberRefreshRequest();
        given(jwtUtil.validateToken(request.refreshToken())).willReturn(true);
        given(jwtUtil.extractSubject(request.refreshToken())).willReturn(MemberFixture.memberEmail);
        given(redisService.getRefreshToken(MemberFixture.memberEmail)).willReturn(request.refreshToken());
        given(jwtUtil.createAccessToken(MemberFixture.memberEmail)).willReturn(MemberFixture.newAccessToken);
        given(jwtUtil.createRefreshToken(MemberFixture.memberEmail)).willReturn(MemberFixture.newRefreshToken);

        MemberRefreshResponse response = memberService.refresh(request);

        assertThat(response.accessToken()).isEqualTo(MemberFixture.newAccessToken);
        assertThat(response.refreshToken()).isEqualTo(MemberFixture.newRefreshToken);
        verify(redisService).saveRefreshToken(anyString(), anyString(), anyLong());
    }

    @Test
    @DisplayName("RefreshToken 으로 토큰 재발급 실패 - 유효하지 않은 토큰")
    void refreshToken_RTR_fail_invalidToken() {
        MemberRefreshRequest request = MemberFixture.memberRefreshRequest();
        given(jwtUtil.validateToken(request.refreshToken())).willReturn(false);

        assertThatThrownBy(() -> memberService.refresh(request))
                .isInstanceOf(ServiceErrorException.class)
                .hasMessageContaining("유효하지 않은 토큰입니다");
        verify(redisService, never()).saveRefreshToken(anyString(), anyString(), anyLong());
    }

    @Test
    @DisplayName("RefreshToken 으로 토큰 재발급 실패 - 레디스에 토큰과 다름")
    void refreshToken_RTR_fail_null() {
        MemberRefreshRequest request = MemberFixture.memberRefreshRequest();
        given(jwtUtil.validateToken(request.refreshToken())).willReturn(true);
        given(jwtUtil.extractSubject(request.refreshToken())).willReturn(MemberFixture.memberEmail);
        given(redisService.getRefreshToken(MemberFixture.memberEmail)).willReturn(MemberFixture.wrongToken);

        assertThatThrownBy(() -> memberService.refresh(request))
                .isInstanceOf(ServiceErrorException.class)
                .hasMessageContaining("유효하지 않은 토큰입니다");
        verify(redisService, never()).saveRefreshToken(anyString(), anyString(), anyLong());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logOut() {
        given(jwtUtil.validateToken(MemberFixture.accessToken)).willReturn(true);
        given(jwtUtil.extractSubject(MemberFixture.accessToken)).willReturn(MemberFixture.memberEmail);

        memberService.logOut(MemberFixture.accessToken);

        verify(redisService).deleteRefreshToken(MemberFixture.memberEmail);
        verify(redisService).addBlacklist(MemberFixture.accessToken, jwtUtil.getRemainingTime(MemberFixture.accessToken));
    }

    @Test
    @DisplayName("로그아웃 실패 - 유효하지 않은 토큰")
    void logOut_fail_invalidToken() {
        given(jwtUtil.validateToken(MemberFixture.accessToken)).willReturn(false);

        assertThatThrownBy(() -> memberService.logOut(MemberFixture.accessToken))
                .isInstanceOf(ServiceErrorException.class)
                .hasMessageContaining("유효하지 않은 토큰입니다");

        verify(redisService, never()).deleteRefreshToken(anyString());
        verify(redisService, never()).addBlacklist(MemberFixture.accessToken, jwtUtil.getRemainingTime(MemberFixture.accessToken));
    }
}
