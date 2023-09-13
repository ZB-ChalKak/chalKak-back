package com.btb.chalKak.domain.styleTag.service.impl;

import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.repository.StyleTagRepository;
import com.btb.chalKak.domain.styleTag.service.StyleTagService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StyleTagServiceImpl implements StyleTagService {

    private final StyleTagRepository styleTagRepository;

    @Override
    public List<StyleTag> loadStyleTags() {
        return styleTagRepository.findAll();
    }
}
