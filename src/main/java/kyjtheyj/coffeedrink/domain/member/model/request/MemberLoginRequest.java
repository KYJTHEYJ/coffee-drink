package kyjtheyj.coffeedrink.domain.member.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberLoginRequest(
        @NotBlank(message = "이메일은 필수입니다")
        @Email(message = "이메일 형식이 아닙니다")
        String email

        , @NotBlank(message = "비밀번호는 필수입니다")
        @Size(min = 8, max = 30, message = "비밀번호는 8~30자 사이입니다")
        String pwd
) {
}
