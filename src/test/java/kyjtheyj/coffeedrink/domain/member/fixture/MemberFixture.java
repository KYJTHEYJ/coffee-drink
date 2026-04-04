package kyjtheyj.coffeedrink.domain.member.fixture;

import kyjtheyj.coffeedrink.domain.member.model.request.MemberRegisterRequest;
import kyjtheyj.coffeedrink.domain.member.model.response.MemberRegisterResponse;
import kyjtheyj.coffeedrink.domain.member.entity.MemberEntity;
import kyjtheyj.coffeedrink.domain.member.entity.MemberRole;

import java.util.UUID;

public class MemberFixture {
    public static final UUID memberId = UUID.randomUUID();
    public static final String memberEmail = "test@test.com";
    public static final String memberPassword = "test1234";
    public static final String memberEncodePassword = "encode_test1234";
    public static final String memberName = "테스터";

    public static MemberRegisterRequest memberRegisterRequest() {
        return new MemberRegisterRequest(memberEmail, memberPassword, memberName);
    }

    public static MemberRegisterResponse memberRegisterResponse() {
        return new MemberRegisterResponse(memberId, memberEmail, memberPassword);
    }

    public static MemberEntity memberWithRoleUser() {
        return MemberEntity.register(memberEmail, memberName, memberEncodePassword, MemberRole.ROLE_USER);
    }
}
