package kyjtheyj.coffeedrink.domain.member.service;

import kyjtheyj.coffeedrink.common.exception.ServiceErrorException;
import kyjtheyj.coffeedrink.domain.member.model.request.MemberRegisterRequest;
import kyjtheyj.coffeedrink.domain.member.model.response.MemberRegisterResponse;
import kyjtheyj.coffeedrink.domain.member.fixture.MemberFixture;
import kyjtheyj.coffeedrink.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    MemberRepository memberRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("일반 회원가입 성공")
    void signUp() {
        MemberRegisterRequest request = MemberFixture.memberRegisterRequest();

        given(memberRepository.findMemberEntityByEmail(request.email())).willReturn(Optional.empty());
        given(passwordEncoder.encode(request.password())).willReturn(MemberFixture.memberEncodePassword);

        MemberRegisterResponse response = memberService.signUp(request);

        assertThat(response.email()).isEqualTo(request.email());
    }

    @Test
    @DisplayName("일반 회원가입 실패 - 이메일 중복")
    void signUpFail_emailDup() {
        MemberRegisterRequest request = MemberFixture.memberRegisterRequest();

        given(memberRepository.findMemberEntityByEmail(request.email())).willReturn(Optional.of(MemberFixture.memberWithRoleUser()));

        assertThatThrownBy(() -> memberService.signUp(request))
                .isInstanceOf(ServiceErrorException.class)
                .hasMessageContaining("이미 사용 중인 이메일입니다");
    }
}
