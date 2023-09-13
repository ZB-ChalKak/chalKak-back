package com.btb.chalKak.domain.filter.controller;

import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.domain.filter.dto.HashTagFilterDto;
import com.btb.chalKak.domain.filter.dto.StyleTagFilterDto;
import com.btb.chalKak.domain.filter.dto.response.MemberFilterResponse;
import com.btb.chalKak.domain.filter.dto.response.PostFilterResponse;
import com.btb.chalKak.domain.filter.dto.response.TagFilterResponse;
import com.btb.chalKak.domain.filter.service.FilterService;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.btb.chalKak.common.exception.type.SuccessCode.*;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
public class FilterController {

    private final FilterService filterService;
    private final ResponseService responseService;

    @GetMapping("/users/{keyword}")
    public ResponseEntity<?> loadUsersByKeyword(@PathVariable("keyword") String keyword,
                                                Pageable pageable){
        List<MemberFilterResponse> users = filterService.loadUsersByKeyword(keyword, pageable);
        return ResponseEntity.ok(responseService.success(users, SUCCESS_LOAD_USER_INFO));
    }

    @GetMapping("/posts")
    public ResponseEntity<?> loadPostsByKeyword(@RequestParam("keyword") String keyword,
                                                @RequestParam("max-length") Long maxLength,
                                                Pageable pageable){
        List<PostFilterResponse> posts = filterService.loadPostsByKeyword(keyword, maxLength, pageable);
        return ResponseEntity.ok(responseService.success(posts, SUCCESS_LOAD_POST));
    }

    @GetMapping("/hash-tags/{keyword}")
    public ResponseEntity<?> loadHashTagsByKeyword(@PathVariable("keyword") String keyword,
                                               Pageable pageable){
        List<HashTagFilterDto> hashTags = filterService.loadHashTagsByKeyword(keyword, pageable);
        return ResponseEntity.ok(responseService.success(hashTags, SUCCESS_LOAD_TAG));
    }

    @GetMapping("/style-tags/{keyword}")
    public ResponseEntity<?> loadStyleTagsByKeyword(@PathVariable("keyword") String keyword,
                                               Pageable pageable){
        List<StyleTagFilterDto> styleTags = filterService.loadStyleTagsByKeyword(keyword, pageable);
        return ResponseEntity.ok(responseService.success(styleTags, SUCCESS_LOAD_TAG));
    }
}
