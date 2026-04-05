package kyjtheyj.coffeedrink.common.model;

import java.util.UUID;

public record MemberPrincipal(
        UUID memberId
        , String email
        , String role
) {
}
