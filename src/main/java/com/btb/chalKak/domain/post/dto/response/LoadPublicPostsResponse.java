package com.btb.chalKak.domain.post.dto.response;

import com.btb.chalKak.domain.post.entity.Post;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class LoadPublicPostsResponse {

    private int currentPage;
    private int totalPages;

    private long totalElements;

    private List<LoadPublicPostDetailsResponse> posts;

    public static LoadPublicPostsResponse fromPage(Page<Post> page) {
        List<LoadPublicPostDetailsResponse> loadPublicPostDetailsResponses =
                page.getContent()
                        .stream()
                        .map(LoadPublicPostDetailsResponse::fromEntity)
                        .collect(Collectors.toList());

        return LoadPublicPostsResponse.builder()
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalElements(page.getTotalElements())
                .posts(loadPublicPostDetailsResponses)
                .build();
    }
}
