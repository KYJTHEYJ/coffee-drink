package kyjtheyj.coffeedrink.domain.member.controller;

import kyjtheyj.coffeedrink.common.config.jwt.JwtUtil;
import kyjtheyj.coffeedrink.common.config.security.SecurityConfig;
import kyjtheyj.coffeedrink.common.service.RedisService;
import kyjtheyj.coffeedrink.domain.member.fixture.MemberFixture;
import kyjtheyj.coffeedrink.domain.member.model.request.MemberLoginRequest;
import kyjtheyj.coffeedrink.domain.member.model.response.MemberLoginResponse;
import kyjtheyj.coffeedrink.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@WebMvcTest(MemberController.class)
@Import(SecurityConfig.class)
public class MemberControllerTest {
    @Autowired
    private MockMvcTester mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MemberService memberService;

    // Filter 를 mock 하면 컨트롤러 도달 실패로 항상 403
    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private RedisService redisService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("로그인 성공")
    public void signIn() {
        MemberLoginRequest request = MemberFixture.memberLoginRequest();
        MemberLoginResponse response = MemberFixture.memberLoginResponse();

        Authentication mockAuthentication = MemberFixture.userRoleAuthentication;
        given(authenticationManager.authenticate(any())).willReturn(mockAuthentication);
        given(memberService.signIn(any(), any())).willReturn(response);

        assertThat(mockMvc.post()
                .uri("/v1/api/members/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.success").asBoolean().isEqualTo(true);
    }

    @Test
    @DisplayName("로그인 실패 - 이메일 또는 비밀번호 문제")
    public void signIn_fail_invalid_email_pwd() {
        MemberLoginRequest request = MemberFixture.memberLoginRequest();

        given(authenticationManager.authenticate(any())).willThrow(new BadCredentialsException("로그인 실패"));

        assertThat(mockMvc.post()
                .uri("/v1/api/members/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatus(HttpStatus.UNAUTHORIZED)
                .bodyJson()
                .extractingPath("$.success").asBoolean().isEqualTo(false);

        // any() 사용 - mock() 은 호출마다 새 인스턴스라 매칭 불가
        verify(memberService, never()).signIn(any(), any());
    }
}
