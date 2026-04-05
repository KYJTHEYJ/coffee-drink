package kyjtheyj.coffeedrink.domain.point.service;

import kyjtheyj.coffeedrink.common.exception.ServiceErrorException;
import kyjtheyj.coffeedrink.common.model.kafka.event.PointLogEvent;
import kyjtheyj.coffeedrink.domain.member.fixture.MemberFixture;
import kyjtheyj.coffeedrink.domain.member.repository.MemberRepository;
import kyjtheyj.coffeedrink.domain.point.entity.PointLogType;
import kyjtheyj.coffeedrink.domain.point.fixture.PointFixture;
import kyjtheyj.coffeedrink.domain.point.model.response.PointChargeResponse;
import kyjtheyj.coffeedrink.domain.point.repository.PointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

    @Mock
    PointRepository pointRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @InjectMocks
    PointService pointService;

    @Test
    @DisplayName("포인트 충전 성공 - CHARGE 이벤트 발행")
    void chargePoint() {
        given(memberRepository.findMemberEntityById(PointFixture.memberId)).willReturn(Optional.of(MemberFixture.memberWithRoleUser()));
        given(pointRepository.findByMemberId(any())).willReturn(Optional.of(PointFixture.pointEntity()));

        PointChargeResponse response = pointService.chargePoint(PointFixture.memberId, PointFixture.chargeAmount);

        assertThat(response.balance()).isEqualTo(PointFixture.chargeAmount);

        // ArgumentCaptor -> 메서드에 전달된 인자를 캡처하여 내부 값을 검증할 때 씀
        ArgumentCaptor<PointLogEvent> captor = ArgumentCaptor.forClass(PointLogEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());

        PointLogEvent publishedEvent = captor.getValue();
        assertThat(publishedEvent.type()).isEqualTo(PointLogType.CHARGE);
        assertThat(publishedEvent.amount()).isEqualTo(PointFixture.chargeAmount);
        assertThat(publishedEvent.orderId()).isNull();
    }

    @Test
    @DisplayName("포인트 충전 실패 - 존재하지 않는 회원")
    void chargePoint_fail_memberNotFound() {
        given(memberRepository.findMemberEntityById(PointFixture.memberId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> pointService.chargePoint(PointFixture.memberId, PointFixture.chargeAmount))
                .isInstanceOf(ServiceErrorException.class)
                .hasMessageContaining("존재하지 않는 회원입니다");

        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("포인트 충전 실패 - 포인트 정보 없음")
    void chargePoint_fail_pointNotFound() {
        given(memberRepository.findMemberEntityById(PointFixture.memberId)).willReturn(Optional.of(MemberFixture.memberWithRoleUser()));
        given(pointRepository.findByMemberId(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> pointService.chargePoint(PointFixture.memberId, PointFixture.chargeAmount))
                .isInstanceOf(ServiceErrorException.class)
                .hasMessageContaining("포인트 정보가 존재하지 않아 진행할 수 없습니다");

        verify(eventPublisher, never()).publishEvent(any());
    }
}
