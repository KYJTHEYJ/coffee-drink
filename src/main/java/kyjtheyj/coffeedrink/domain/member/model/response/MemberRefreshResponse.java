package kyjtheyj.coffeedrink.domain.member.model.response;

public record MemberRefreshResponse(
        String accessToken
        , String refreshToken
) {
}
