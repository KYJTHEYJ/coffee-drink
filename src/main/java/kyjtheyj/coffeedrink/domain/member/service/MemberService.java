package kyjtheyj.coffeedrink.domain.member.service;

import kyjtheyj.coffeedrink.common.config.jwt.JwtUtil;
import kyjtheyj.coffeedrink.common.exception.ServiceErrorException;
import kyjtheyj.coffeedrink.common.model.principal.MemberPrincipal;
import kyjtheyj.coffeedrink.common.service.RedisService;
import kyjtheyj.coffeedrink.domain.member.entity.MemberEntity;
import kyjtheyj.coffeedrink.domain.member.entity.MemberRole;
import kyjtheyj.coffeedrink.domain.member.model.request.MemberLoginRequest;
import kyjtheyj.coffeedrink.domain.member.model.request.MemberRegisterRequest;
import kyjtheyj.coffeedrink.domain.member.model.request.MemberRefreshRequest;
import kyjtheyj.coffeedrink.domain.member.model.response.MemberRefreshResponse;
import kyjtheyj.coffeedrink.domain.member.model.response.MemberLoginResponse;
import kyjtheyj.coffeedrink.domain.member.model.response.MemberRegisterResponse;
import kyjtheyj.coffeedrink.domain.member.repository.MemberRepository;
import kyjtheyj.coffeedrink.domain.point.entity.PointEntity;
import kyjtheyj.coffeedrink.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kyjtheyj.coffeedrink.common.exception.domain.MemberExceptionEnum.ERR_MEMBER_EMAIL_DUPLICATED;
import static kyjtheyj.coffeedrink.common.exception.domain.MemberExceptionEnum.ERR_MEMBER_NOT_FOUND;
import static kyjtheyj.coffeedrink.common.exception.domain.MemberExceptionEnum.ERR_TOKEN_INVALID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @Value("${jwt.secret.refreshExpire}")
    private long refreshTokenExpire;

    @Transactional
    public MemberRegisterResponse signUp(MemberRegisterRequest request) {
        if (memberRepository.findMemberEntityByEmail(request.email()).isPresent()) {
            throw new ServiceErrorException(ERR_MEMBER_EMAIL_DUPLICATED);
        }

        String encodedPwd = passwordEncoder.encode(request.pwd());
        MemberEntity member = MemberEntity.register(request.email(), request.name(), encodedPwd, MemberRole.ROLE_USER);
        memberRepository.save(member);

        PointEntity point = PointEntity.register(member.getId());
        pointRepository.save(point);

        return MemberRegisterResponse.register(member);
    }

    public MemberLoginResponse signIn(MemberLoginRequest request, Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        MemberEntity member = memberRepository.findMemberEntityByEmail(request.email())
                .orElseThrow(() -> new ServiceErrorException(ERR_MEMBER_NOT_FOUND));

        String accessToken = jwtUtil.createAccessToken(request.email(), member.getId(), role);
        String refreshToken = jwtUtil.createRefreshToken(request.email());

        redisService.saveRefreshToken(request.email(), refreshToken, refreshTokenExpire);

        return new MemberLoginResponse(accessToken, refreshToken);
    }

    public MemberRefreshResponse refresh(MemberRefreshRequest request, MemberPrincipal memberInfo) {
        String refreshToken = request.refreshToken();

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new ServiceErrorException(ERR_TOKEN_INVALID);
        }

        String email = jwtUtil.extractSubject(refreshToken);
        String savedToken = redisService.getRefreshToken(email);

        if (!savedToken.equals(refreshToken)) {
            throw new ServiceErrorException(ERR_TOKEN_INVALID);
        }

        String newAccessToken = jwtUtil.createAccessToken(email, memberInfo.memberId(), memberInfo.role());
        String newRefreshToken = jwtUtil.createRefreshToken(email);

        redisService.saveRefreshToken(email, newRefreshToken, refreshTokenExpire);

        return new MemberRefreshResponse(newAccessToken, newRefreshToken);
    }

    public void logOut(String accessToken) {
        if (!jwtUtil.validateToken(accessToken)) {
            throw new ServiceErrorException(ERR_TOKEN_INVALID);
        }

        String email = jwtUtil.extractSubject(accessToken);
        redisService.deleteRefreshToken(email);
        redisService.addBlacklist(accessToken, jwtUtil.getRemainingTime(accessToken));
    }
}
