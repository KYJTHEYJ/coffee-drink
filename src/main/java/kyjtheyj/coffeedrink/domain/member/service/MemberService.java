package kyjtheyj.coffeedrink.domain.member.service;

import kyjtheyj.coffeedrink.common.exception.ServiceErrorException;
import kyjtheyj.coffeedrink.domain.member.model.request.MemberRegisterRequest;
import kyjtheyj.coffeedrink.domain.member.model.response.MemberRegisterResponse;
import kyjtheyj.coffeedrink.domain.member.entity.MemberEntity;
import kyjtheyj.coffeedrink.domain.member.entity.MemberRole;
import kyjtheyj.coffeedrink.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kyjtheyj.coffeedrink.common.exception.domain.MemberExceptionEnum.ERR_MEMBER_EMAIL_DUPLICATED;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberRegisterResponse signUp(MemberRegisterRequest request) {
        if (memberRepository.findMemberEntityByEmail(request.email()).isPresent()) {
            throw new ServiceErrorException(ERR_MEMBER_EMAIL_DUPLICATED);
        }

        String encodedPwd = passwordEncoder.encode(request.pwd());
        MemberEntity member = MemberEntity.register(request.email(), request.name(), encodedPwd, MemberRole.ROLE_USER);
        memberRepository.save(member);

        return MemberRegisterResponse.register(member);
    }
}
