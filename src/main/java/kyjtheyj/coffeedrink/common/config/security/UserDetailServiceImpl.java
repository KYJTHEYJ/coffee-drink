package kyjtheyj.coffeedrink.common.config.security;

import kyjtheyj.coffeedrink.domain.member.entity.MemberEntity;
import kyjtheyj.coffeedrink.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        MemberEntity member = memberRepository.findMemberEntityByEmail(email).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다"));

        return User.builder()
                .username(member.getEmail())
                .password(member.getPwd())
                .authorities(new SimpleGrantedAuthority(member.getRole().name()))
                .build();
    }
}
