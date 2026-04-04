package kyjtheyj.coffeedrink.domain.member.model.request;

import jakarta.validation.constraints.NotBlank;

public record MemberRefreshRequest(
        @NotBlank(message = "토큰은 필수 입니다")
        String refreshToken
) {
}
