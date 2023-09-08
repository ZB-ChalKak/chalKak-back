package com.btb.chalKak.domain.batchpost.entity;

import static com.btb.chalKak.domain.post.type.PostStatus.DELETED;
import static com.btb.chalKak.domain.post.type.PostStatus.PUBLIC;

import com.btb.chalKak.common.entity.BaseTimeEntity;
import com.btb.chalKak.domain.hashTag.entity.HashTag;
import com.btb.chalKak.domain.like.entity.Like;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.photo.entity.Photo;
import com.btb.chalKak.domain.post.dto.EditPost;
import com.btb.chalKak.domain.post.type.PostStatus;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
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
public class RecommendPostBatch extends BaseTimeEntity {

    @Id
    @Column(name ="post_id", nullable = false)
    private Long id;

    @Column(name = "weather_id", nullable = false)
    private Long weatherId;
    @Column(name = "season_id", nullable = false)
    private Long seasonId;
    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L;


//    private List<Long> styleTags;

}
