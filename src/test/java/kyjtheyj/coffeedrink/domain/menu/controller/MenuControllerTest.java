package kyjtheyj.coffeedrink.domain.menu.controller;

import kyjtheyj.coffeedrink.common.config.jwt.JwtUtil;
import kyjtheyj.coffeedrink.common.config.security.SecurityConfig;
import kyjtheyj.coffeedrink.common.service.RedisService;
import kyjtheyj.coffeedrink.domain.member.fixture.MemberFixture;
import kyjtheyj.coffeedrink.domain.menu.fixture.MenuFixture;
import kyjtheyj.coffeedrink.domain.menu.model.request.MenuRegisterRequest;
import kyjtheyj.coffeedrink.domain.menu.service.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tools.jackson.databind.ObjectMapper;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@WebMvcTest(MenuController.class)
@Import(SecurityConfig.class)
public class MenuControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MenuService menuService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private RedisService redisService;

    @Test
    @DisplayName("메뉴 등록 성공 - 관리자")
    void register() {
        MenuRegisterRequest request = MenuFixture.menuRegisterRequest();

        // 토큰 유효, 블랙리스트 미등록, ADMIN 권한 유저 반환
        given(jwtUtil.validateToken(MenuFixture.adminToken)).willReturn(true);
        given(redisService.isBlacklist(MenuFixture.adminToken)).willReturn(false);
        given(jwtUtil.extractSubject(MenuFixture.adminToken)).willReturn(MenuFixture.adminEmail);
        given(jwtUtil.extractRoleByToken(MenuFixture.adminToken)).willReturn(MemberFixture.memberAdminRole);
        given(jwtUtil.extractMemberIdByToken(MenuFixture.adminToken)).willReturn(MemberFixture.memberId.toString());

        given(menuService.register(any())).willReturn(MenuFixture.menuRegisterResponse());

        assertThat(mockMvc.post()
                .uri("/v1/api/menus")
                .header("Authorization", "Bearer " + MenuFixture.adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .extractingPath("$.success").asBoolean().isEqualTo(true);
    }

    @Test
    @DisplayName("메뉴 등록 실패 - 권한 없음")
    void register_fail_forbidden() {
        MenuRegisterRequest request = MenuFixture.menuRegisterRequest();

        // 토큰 유효, 블랙리스트 미등록, USER 권한 유저 반환
        given(jwtUtil.validateToken(MenuFixture.userToken)).willReturn(true);
        given(redisService.isBlacklist(MenuFixture.userToken)).willReturn(false);
        given(jwtUtil.extractSubject(MenuFixture.userToken)).willReturn(MenuFixture.userEmail);
        given(jwtUtil.extractRoleByToken(MenuFixture.userToken)).willReturn(MemberFixture.memberUserRole);
        given(jwtUtil.extractMemberIdByToken(MenuFixture.userToken)).willReturn(MemberFixture.memberId.toString());

        assertThat(mockMvc.post()
                .uri("/v1/api/menus")
                .header("Authorization", "Bearer " + MenuFixture.userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatus(HttpStatus.FORBIDDEN);

        verify(menuService, never()).register(any());
    }

    @Test
    @DisplayName("메뉴 등록 실패 - 인증 정보 없음")
    void register_fail_unauthorized() {
        MenuRegisterRequest request = MenuFixture.menuRegisterRequest();

        assertThat(mockMvc.post()
                .uri("/v1/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatus(HttpStatus.UNAUTHORIZED);

        verify(menuService, never()).register(any());
    }

    @Test
    @DisplayName("메뉴 등록 실패 - 유효성 검사 오류")
    void register_fail_validation() {
        MenuRegisterRequest request = new MenuRegisterRequest("", BigInteger.valueOf(4500), "설명", 1, 100L);

        given(jwtUtil.validateToken(MenuFixture.adminToken)).willReturn(true);
        given(redisService.isBlacklist(MenuFixture.adminToken)).willReturn(false);
        given(jwtUtil.extractSubject(MenuFixture.adminToken)).willReturn(MenuFixture.adminEmail);
        given(jwtUtil.extractRoleByToken(MenuFixture.adminToken)).willReturn(MemberFixture.memberAdminRole);
        given(jwtUtil.extractMemberIdByToken(MenuFixture.adminToken)).willReturn(MemberFixture.memberId.toString());

        assertThat(mockMvc.post()
                .uri("/v1/api/menus")
                .header("Authorization", "Bearer " + MenuFixture.adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson()
                .extractingPath("$.success").asBoolean().isEqualTo(false);

        verify(menuService, never()).register(any());
    }
}
