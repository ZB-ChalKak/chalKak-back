package com.btb.chalKak.domain.filter.controller;

import com.btb.chalKak.common.response.service.ResponseService;
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
    public ResponseEntity<?> loadUsersByKeyword(@PathVariable String keyword,
                                                Pageable pageable){
        Page<MemberFilterResponse> users = filterService.loadUsersByKeyword(keyword, pageable);
        return ResponseEntity.ok(responseService.success(users, SUCCESS_LOAD_USER_INFO));
    }

    @GetMapping("/posts")
    public ResponseEntity<?> loadPostsByKeyword(@RequestParam String keyword,
                                                @RequestParam Long length,
                                                Pageable pageable){
        Page<PostFilterResponse> posts = filterService.loadPostsByKeyword(keyword, length, pageable);
        return ResponseEntity.ok(responseService.success(posts, SUCCESS_LOAD_POST));
    }

    @GetMapping("/tags/{keyword}")
    public ResponseEntity<?> loadTagsByKeyword(@PathVariable String keyword,
                                               Pageable pageable){
        TagFilterResponse tags = filterService.loadTagsByKeyword(keyword, pageable);
        return ResponseEntity.ok(responseService.success(tags, SUCCESS_LOAD_TAG));
    }
}
