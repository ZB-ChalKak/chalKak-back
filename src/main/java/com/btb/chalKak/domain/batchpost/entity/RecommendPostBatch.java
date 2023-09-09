package com.btb.chalKak.domain.batchpost.entity;

import static com.btb.chalKak.domain.post.type.PostStatus.DELETED;
import static com.btb.chalKak.domain.post.type.PostStatus.PUBLIC;

import com.btb.chalKak.common.entity.BaseTimeEntity;
import com.btb.chalKak.domain.hashTag.entity.HashTag;
import com.btb.chalKak.domain.like.entity.Like;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.photo.entity.Photo;
import com.btb.chalKak.domain.post.dto.EditPost;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.type.PostStatus;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recommend_post")
public class RecommendPostBatch {

    @Id
    @Column(name = "post_id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.REMOVE)
    @MapsId
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "weather_id", nullable = false)
    private Long weatherId;
    @Column(name = "season_id", nullable = false)
    private Long seasonId;
    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L;

    @Column(name = "style_tag_ids")
    private String styleTagIds;


    @Transient
    public List<Long> getStyleTagIdsList() {
        if (styleTagIds == null || styleTagIds.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(styleTagIds.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    public void updateStyleTagIdsList(List<Long> newStyleTagIds) {
        this.styleTagIds = newStyleTagIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

}
