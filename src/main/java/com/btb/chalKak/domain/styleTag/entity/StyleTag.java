package com.btb.chalKak.domain.styleTag.entity;

import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.styleTag.type.StyleCategory;
import com.btb.chalKak.global.entity.BaseTimeEntity;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "style_tag")
public class StyleTag extends BaseTimeEntity {

    @Id
    @Column(name ="style_tag_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private StyleCategory category;

    @Column(name = "keyword", nullable = false, unique = true)
    private String keyword;

    @ManyToMany(mappedBy = "styleTags")
    private List<Member> members;

    @ManyToMany(mappedBy = "styleTags")
    private List<Post> posts;

}