package kyjtheyj.coffeedrink.domain.member.repository;

import kyjtheyj.coffeedrink.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<MemberEntity, UUID> {
    Optional<MemberEntity> findMemberEntityByEmail(String email);
}
