package com.btb.chalKak.domain.member.entity;

import com.btb.chalKak.domain.member.type.Gender;
import com.btb.chalKak.domain.member.type.MemberRole;
import com.btb.chalKak.domain.member.type.MemberStatus;
import com.btb.chalKak.domain.staticTag.entity.StaticTag;
import com.btb.chalKak.global.entity.BaseTimeEntity;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id
    @Column(name ="member_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "height")
    private double height;
    @Column(name = "weight")
    private double weight;

    @Column(name = "privacy_height")
    private boolean privacyHeight;
    @Column(name = "privacy_weight")
    private boolean privacyWeight;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @ManyToMany
    @JoinTable(
            name = "member_static_tag",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "static_tag_id")
    )
    private List<StaticTag> staticTags;

    public Member update(String name, String profileImageUrl) {
        this.nickname = name;
        this.profileImg = profileImageUrl;

        return this;
    }
}
