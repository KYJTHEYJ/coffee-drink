package kyjtheyj.coffeedrink.domain.member.model.response;

public record MemberLoginResponse(
        String accessToken
        , String refreshToken
) {
}
