package com.btb.chalKak.domain.filter.service;

import java.util.List;

public interface FilterService {
    List<Long> loadUsersByKeyword(String keyword);

    List<Long> loadPostsByKeyword(String keyword);

    List<Long> loadTagsByKeyword(String keyword);
}
