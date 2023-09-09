package com.btb.chalKak.domain.post.entity;

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
@Table(name = "post")
public class Post extends BaseTimeEntity {

    @Id
    @Column(name ="post_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "content")
    private String content;

    @Builder.Default
    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    @Builder.Default
    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L;

    @Column(name = "privacy_height", nullable = false)
    private boolean privacyHeight;

    @Column(name = "privacy_weight", nullable = false)
    private boolean privacyWeight;

    @Builder.Default
    @Column(name = "location")
    private String location = "";

    @Builder.Default
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus status = PUBLIC;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Like> likes;

    @Transient
    private boolean liked;
    @Transient
    private boolean following;

    @Transient
    private String thumbnail;

    @ManyToMany
    @JoinTable(
            name = "post_style_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "style_tag_id")
    )
    private List<StyleTag> styleTags;

    @ManyToMany
    @JoinTable(
            name = "post_hash_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "hash_tag_id")
    )
    private List<HashTag> hashTags;

    public Post delete() {
        this.status = DELETED;
        return this;
    }

    public void updateStyleTags(List<StyleTag> editedStyleTags) {
        this.styleTags = editedStyleTags;
    }
    public void updatePhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public void updateHashTags(List<HashTag> editedHashTags) {
        this.hashTags = editedHashTags;
    }

    public Post edit(EditPost editPost) {
        this.content = editPost.getContent();
        this.location = editPost.getLocation();
        this.privacyHeight = editPost.isPrivacyHeight();
        this.privacyWeight = editPost.isPrivacyWeight();
        return this;
    }

    public void updateIsFollowingAndIsLiked(boolean isFollowing, boolean isLiked) {
        this.following = isFollowing;
        this.liked = isLiked;
    }

    public void updateIsLiked(boolean isLiked) {
        this.liked = isLiked;
    }

    public String getThumbnail() {
        return photos.isEmpty() ? null : photos.get(0).getUrl();
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }
}
