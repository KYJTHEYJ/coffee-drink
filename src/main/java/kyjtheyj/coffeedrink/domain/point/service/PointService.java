package kyjtheyj.coffeedrink.domain.point.service;

import kyjtheyj.coffeedrink.common.annotation.DistributedLock;
import kyjtheyj.coffeedrink.common.exception.ServiceErrorException;
import kyjtheyj.coffeedrink.domain.member.entity.MemberEntity;
import kyjtheyj.coffeedrink.domain.member.repository.MemberRepository;
import kyjtheyj.coffeedrink.domain.point.entity.PointEntity;
import kyjtheyj.coffeedrink.domain.point.model.response.PointAddResponse;
import kyjtheyj.coffeedrink.domain.point.repository.PointLogRepository;
import kyjtheyj.coffeedrink.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

import static kyjtheyj.coffeedrink.common.exception.domain.MemberExceptionEnum.ERR_MEMBER_NOT_FOUND;
import static kyjtheyj.coffeedrink.common.exception.domain.PointExceptionEnum.ERR_POINT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final PointLogRepository pointLogRepository;
    private final MemberRepository memberRepository;

    @DistributedLock(key = "'point:' + #memberEmail", waitTime = 10L)
    public PointAddResponse addPoint(String memberEmail, BigInteger addPoint) {
        MemberEntity member = memberRepository.findMemberEntityByEmail(memberEmail).orElseThrow(() -> new ServiceErrorException(ERR_MEMBER_NOT_FOUND));

        PointEntity point = pointRepository.findByMemberId(member.getId()).orElseThrow(() -> new ServiceErrorException(ERR_POINT_NOT_FOUND));

        point.add(addPoint);

        return new PointAddResponse(point.getBalance());
    }

}
