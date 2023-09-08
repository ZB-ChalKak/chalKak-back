package com.btb.chalKak.domain.post.dto.response;

import com.btb.chalKak.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class LoadUserPublicPostsResponse {
    private int currentPage;
    private int totalPages;

    private long totalElements;

    private boolean isAuthenticated;

    private List<LoadUserPublicPostPreviewResponse> posts;

    public static LoadUserPublicPostsResponse fromPage(Page<Post> page) {
        List<LoadUserPublicPostPreviewResponse> loadUserPublicPostPreviewResponse =
                page.getContent()
                        .stream()
                        .map(LoadUserPublicPostPreviewResponse::fromEntity)
                        .collect(Collectors.toList());

        return LoadUserPublicPostsResponse.builder()
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalElements(page.getTotalElements())
                .posts(loadUserPublicPostPreviewResponse)
                .build();
    }

    public void updateAuthenticated() {
        this.isAuthenticated = true;
    }

}
