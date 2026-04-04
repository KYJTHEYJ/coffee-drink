package kyjtheyj.coffeedrink.domain.member.entity;

import jakarta.persistence.*;
import kyjtheyj.coffeedrink.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "members", uniqueConstraints = {
        @UniqueConstraint(name = "uk_members_id", columnNames = {"id"})
        , @UniqueConstraint(name = "uk_members_email", columnNames = {"email"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends BaseTimeEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, updatable = false, length = 120)
    private String email;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, length = 300)
    private String pwd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MemberRole role;

    private boolean withdraw = false;

    @Column(name = "withdraw_at")
    private LocalDateTime withdrawAt;

    public static MemberEntity register(String email, String name, String pwd, MemberRole role) {
        MemberEntity entity = new MemberEntity();
        entity.email = email;
        entity.name = name;
        entity.pwd = pwd;
        entity.role = role;
        entity.withdraw = false;
        entity.withdrawAt = null;
        return entity;
    }

    public void withdraw() {
        this.withdraw = true;
        this.withdrawAt = LocalDateTime.now();
    }

    public void updatePwd(String pwd) {
        this.pwd = pwd;
    }

    public void updateRole(MemberRole role) {
        this.role = role;
    }
}
