package com.btb.chalKak.domain.member.entity;

import static com.btb.chalKak.domain.member.type.MemberRole.USER;
import static com.btb.chalKak.domain.member.type.MemberStatus.ACTIVE;
import static com.btb.chalKak.domain.member.type.MemberStatus.BLOCKED;
import static com.btb.chalKak.domain.member.type.MemberStatus.WITHDRAWAL;

import com.btb.chalKak.common.entity.BaseTimeEntity;
import com.btb.chalKak.domain.member.type.Gender;
import com.btb.chalKak.domain.member.type.MemberRole;
import com.btb.chalKak.domain.member.type.MemberStatus;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
public class Member extends BaseTimeEntity implements UserDetails {

    @Id
    @Column(name ="member_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "password")
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

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Builder.Default
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role = USER;

    @OneToMany(mappedBy = "writer")
    private List<Post> posts;

    @ManyToMany
    @JoinTable(
            name = "member_style_tag",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "style_tag_id")
    )
    private List<StyleTag> styleTags;

    public Member update(String name, String profileImageUrl) {
        this.nickname = name;
        this.profileImg = profileImageUrl;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        auth.add(new SimpleGrantedAuthority(role.getRole()));
        return auth;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 사용자 계정 만료(탈퇴) 여부 반환
        return this.status != WITHDRAWAL;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 사용자 계정 잠금 여부 반환
        return this.status != BLOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 사용자 인증 정보 만료 여부 반환
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 사용자 계정 활성화 여부 반환
        return this.status == ACTIVE;
    }

}
