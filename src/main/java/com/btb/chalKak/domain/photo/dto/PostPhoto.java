package com.btb.chalKak.domain.photo.dto;

import com.btb.chalKak.domain.photo.entity.Photo;
import java.util.Comparator;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostPhoto {

    private Long id;
    private Integer order;
    private String name;
    private String url;

    public static PostPhoto fromEntity(Photo photo) {
        return PostPhoto.builder()
                .id(photo.getId())
                .order(photo.getOrder())
                .name(photo.getName())
                .url(photo.getUrl())
                .build();
    }

    public static Comparator<PostPhoto> orderASCByOrder = Comparator.comparingInt(PostPhoto::getOrder);
}
