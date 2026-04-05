package kyjtheyj.coffeedrink.common.model.principal;

import java.util.UUID;

public record MemberPrincipal(
        UUID memberId
        , String email
        , String role
) {
}
