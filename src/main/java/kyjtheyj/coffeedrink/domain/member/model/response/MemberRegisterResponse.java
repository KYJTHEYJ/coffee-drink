package kyjtheyj.coffeedrink.domain.member.model.response;

import kyjtheyj.coffeedrink.domain.member.entity.MemberEntity;

import java.util.UUID;

public record MemberRegisterResponse(
        UUID id,
        String email,
        String name
) {
    public static MemberRegisterResponse register(MemberEntity entity) {
        return new MemberRegisterResponse(entity.getId(), entity.getEmail(), entity.getName());
    }
}
