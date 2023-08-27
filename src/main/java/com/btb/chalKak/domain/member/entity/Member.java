package com.btb.chalKak.domain.member.entity;

import static com.btb.chalKak.domain.member.type.MemberRole.USER;
import static com.btb.chalKak.domain.member.type.MemberStatus.INACTIVE;

import com.btb.chalKak.common.entity.BaseTimeEntity;
import com.btb.chalKak.domain.member.type.Gender;
import com.btb.chalKak.domain.member.type.MemberRole;
import com.btb.chalKak.domain.member.type.MemberStatus;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
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

    @Column(name = "password")
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "height")
    private double height;
    @Column(name = "weight")
    private double weight;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Builder.Default
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus status = INACTIVE;

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

}
