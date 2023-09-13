package com.btb.chalKak.domain.post.dto.response;

import com.btb.chalKak.domain.hashTag.entity.HashTag;
import com.btb.chalKak.domain.member.dto.Writer;
import com.btb.chalKak.domain.photo.dto.PostPhoto;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.type.StyleCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoadPublicPostDetailsResponse {

    private Long id;
    private String content;
    private String location;

    private Long viewCount;
    private Long likeCount;

    private boolean privacyHeight;
    private boolean privacyWeight;

    private List<String> styleTags; // Category -> "STYLE" + "TPO"
    private List<String> seasonTags;
    private List<String> weatherTags;

    private List<String> hashTags;
    private List<PostPhoto> postPhotos;

    private boolean following;
    private boolean liked;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    private Writer writer;

    public static LoadPublicPostDetailsResponse fromEntity(Post post) {
        return LoadPublicPostDetailsResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .location(post.getLocation())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .privacyHeight(post.isPrivacyHeight())
                .privacyWeight(post.isPrivacyWeight())
                .styleTags(post.getStyleTags().stream()
                        .filter(styleTag ->
                                styleTag.getCategory() == StyleCategory.STYLE
                                        || styleTag.getCategory() == StyleCategory.TPO)
                        .map(StyleTag::getKeyword)
                        .collect(Collectors.toList()))
                .seasonTags(post.getStyleTags().stream()
                        .filter(styleTag -> styleTag.getCategory() == StyleCategory.SEASON)
                        .map(StyleTag::getKeyword)
                        .collect(Collectors.toList()))
                .weatherTags(post.getStyleTags().stream()
                        .filter(styleTag -> styleTag.getCategory() == StyleCategory.WEATHER)
                        .map(StyleTag::getKeyword)
                        .collect(Collectors.toList()))
                .hashTags(post.getHashTags().stream()
                        .map(HashTag::getKeyword)
                        .collect(Collectors.toList()))
                .postPhotos(post.getPhotos().stream()
                        .map(PostPhoto::fromEntity)
                        .collect(Collectors.toList()))
                .following(post.isFollowing())
                .liked(post.isLiked())
                .createdAt(post.getCreatedAt())
                .writer(Writer.fromEntity(post.getWriter()))
                .build();
    }

}
