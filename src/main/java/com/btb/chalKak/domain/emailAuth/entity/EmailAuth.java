package com.btb.chalKak.domain.emailAuth.entity;

import static com.btb.chalKak.domain.emailAuth.type.EmailAuthStatus.UNVERIFIED;
import static com.btb.chalKak.domain.emailAuth.type.EmailAuthStatus.VERIFIED;

import com.btb.chalKak.common.entity.BaseTimeEntity;
import com.btb.chalKak.domain.emailAuth.type.EmailAuthStatus;
import com.btb.chalKak.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "email_auth")
public class EmailAuth extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Member member;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmailAuthStatus status;

    @Column(name = "auth_token", nullable = false)
    private String emailAuthToken;

    @Column(name = "auth_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime emailAuthAt;

    public EmailAuth confirmAuth() {
        this.status = VERIFIED;
        this.emailAuthAt = LocalDateTime.now();
        return this;
    }

    public static EmailAuth generateEmailAuth(Member member) {
        return EmailAuth.builder()
                .emailAuthToken(UUID.randomUUID().toString())
                .status(UNVERIFIED)
                .member(member)
                .build();
    }

}
