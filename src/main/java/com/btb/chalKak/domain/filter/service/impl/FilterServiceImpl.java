package com.btb.chalKak.domain.filter.service.impl;

import com.btb.chalKak.domain.filter.service.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService {


    @Override
    @Transactional
    public List<Long> loadUsersByKeyword(String keyword) {
        return null;
    }

    @Override
    @Transactional
    public List<Long> loadPostsByKeyword(String keyword) {
        return null;
    }

    @Override
    @Transactional
    public List<Long> loadTagsByKeyword(String keyword) {
        return null;
    }
}
