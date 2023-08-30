package com.btb.chalKak.domain.styleTag.controller;

import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_LOAD_STYLE_TAG;

import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.domain.styleTag.dto.response.LoadStyleTagsResponse;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.service.StyleTagService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/styleTags")
@RequiredArgsConstructor
public class StyleTagController {

    private final StyleTagService styleTagService;
    private final ResponseService responseService;

    @GetMapping
    public ResponseEntity<?> loadStyleTags() {
        List<StyleTag> styleTags = styleTagService.loadStyleTags();
        LoadStyleTagsResponse data = LoadStyleTagsResponse.fromEntities(styleTags);

        return ResponseEntity.ok(responseService.success(data, SUCCESS_LOAD_STYLE_TAG));
    }

}
