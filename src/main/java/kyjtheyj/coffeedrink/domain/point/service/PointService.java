package kyjtheyj.coffeedrink.domain.point.service;

import kyjtheyj.coffeedrink.common.annotation.DistributedLock;
import kyjtheyj.coffeedrink.common.exception.ServiceErrorException;
import kyjtheyj.coffeedrink.common.model.kafka.event.PointLogEvent;
import kyjtheyj.coffeedrink.domain.member.entity.MemberEntity;
import kyjtheyj.coffeedrink.domain.member.repository.MemberRepository;
import kyjtheyj.coffeedrink.domain.point.entity.PointEntity;
import kyjtheyj.coffeedrink.domain.point.entity.PointLogType;
import kyjtheyj.coffeedrink.domain.point.model.response.PointAddResponse;
import kyjtheyj.coffeedrink.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.UUID;

import static kyjtheyj.coffeedrink.common.exception.domain.MemberExceptionEnum.ERR_MEMBER_NOT_FOUND;
import static kyjtheyj.coffeedrink.common.exception.domain.PointExceptionEnum.ERR_POINT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @DistributedLock(key = "'point:' + #memberId", waitTime = 10L)
    public PointAddResponse chargePoint(UUID memberId, BigInteger addPoint) {
        MemberEntity member = memberRepository.findMemberEntityById(memberId).orElseThrow(() -> new ServiceErrorException(ERR_MEMBER_NOT_FOUND));

        PointEntity point = pointRepository.findByMemberId(member.getId()).orElseThrow(() -> new ServiceErrorException(ERR_POINT_NOT_FOUND));

        point.add(addPoint);

        // 커밋 후에 전달 될 수 있도록 추가
        eventPublisher.publishEvent(new PointLogEvent(member.getId(), null, addPoint, PointLogType.CHARGE));

        return new PointAddResponse(point.getBalance());
    }

}
