package kyjtheyj.coffeedrink.domain.order.controller;

import kyjtheyj.coffeedrink.common.config.jwt.JwtUtil;
import kyjtheyj.coffeedrink.common.config.security.SecurityConfig;
import kyjtheyj.coffeedrink.common.service.RedisService;
import kyjtheyj.coffeedrink.domain.member.fixture.MemberFixture;
import kyjtheyj.coffeedrink.domain.menu.fixture.MenuFixture;
import kyjtheyj.coffeedrink.domain.order.facade.OrderFacade;
import kyjtheyj.coffeedrink.domain.order.fixture.OrderFixture;
import kyjtheyj.coffeedrink.domain.order.model.request.OrderMenuRequest;
import kyjtheyj.coffeedrink.domain.order.model.request.OrderMenuStockRequest;
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

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@WebMvcTest(OrderController.class)
@Import(SecurityConfig.class)
public class OrderControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderFacade orderFacade;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private RedisService redisService;

    @Test
    @DisplayName("주문 생성 성공")
    void orderMenu() {
        given(jwtUtil.validateToken(OrderFixture.userToken)).willReturn(true);
        given(redisService.isBlacklist(OrderFixture.userToken)).willReturn(false);
        given(jwtUtil.extractSubject(OrderFixture.userToken)).willReturn(MemberFixture.memberEmail);
        given(jwtUtil.extractRoleByToken(OrderFixture.userToken)).willReturn(MemberFixture.memberUserRole);
        given(jwtUtil.extractMemberIdByToken(OrderFixture.userToken)).willReturn(MemberFixture.memberId.toString());
        given(orderFacade.orderMenu(any(), any())).willReturn(OrderFixture.orderMenuResponse());

        assertThat(mockMvc.post()
                .uri("/v1/api/orders")
                .header("Authorization", "Bearer " + OrderFixture.userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(OrderFixture.orderMenuRequest())))
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .extractingPath("$.success").asBoolean().isEqualTo(true);
    }

    @Test
    @DisplayName("주문 생성 실패 - 다른 회원 주문 시도")
    void orderMenu_fail_forbidden() {
        given(jwtUtil.validateToken(OrderFixture.userToken)).willReturn(true);
        given(redisService.isBlacklist(OrderFixture.userToken)).willReturn(false);
        given(jwtUtil.extractSubject(OrderFixture.userToken)).willReturn(MemberFixture.memberEmail);
        given(jwtUtil.extractRoleByToken(OrderFixture.userToken)).willReturn(MemberFixture.memberUserRole);
        given(jwtUtil.extractMemberIdByToken(OrderFixture.userToken)).willReturn(MemberFixture.memberId.toString());

        OrderMenuRequest otherMemberRequest = new OrderMenuRequest(
                UUID.randomUUID(),
                List.of(new OrderMenuStockRequest(OrderFixture.menuId, OrderFixture.orderQuantity))
        );

        assertThat(mockMvc.post()
                .uri("/v1/api/orders")
                .header("Authorization", "Bearer " + OrderFixture.userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(otherMemberRequest)))
                .hasStatus(HttpStatus.FORBIDDEN);

        verify(orderFacade, never()).orderMenu(any(), any());
    }

    @Test
    @DisplayName("주문 생성 실패 - 인증 정보 없음")
    void orderMenu_fail_unauthorized() {
        assertThat(mockMvc.post()
                .uri("/v1/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(OrderFixture.orderMenuRequest())))
                .hasStatus(HttpStatus.UNAUTHORIZED);

        verify(orderFacade, never()).orderMenu(any(), any());
    }
}
