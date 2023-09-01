package com.btb.chalKak.domain.filter.controller;

import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.domain.filter.service.FilterService;
import com.btb.chalKak.domain.member.service.MemberService;
import com.btb.chalKak.domain.post.service.PostService;
import com.btb.chalKak.domain.styleTag.service.StyleTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.btb.chalKak.common.exception.type.SuccessCode.*;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
public class FilterController {

    private final FilterService filterService;
    private final ResponseService responseService;

    @GetMapping("/users/{keyword}")
    public ResponseEntity<?> loadUsersByKeyword(@PathVariable String keyword){
        List<Long> users = filterService.loadUsersByKeyword(keyword);
        return ResponseEntity.ok(responseService.success(users, SUCCESS_LOAD_USER_INFO));
    }

    @GetMapping("/posts/{keyword}")
    public ResponseEntity<?> loadPostsByKeyword(@PathVariable String keyword){
        List<Long> posts = filterService.loadPostsByKeyword(keyword);
        return ResponseEntity.ok(responseService.success(posts, SUCCESS_LOAD_POST));
    }

    @GetMapping("/tags/{keyword}")
    public ResponseEntity<?> loadTagsByKeyword(@PathVariable String keyword){
        List<Long> tags = filterService.loadTagsByKeyword(keyword);
        return ResponseEntity.ok(responseService.success(tags, SUCCESS_LOAD_TAG));
    }
}
