package com.ogul.wordfinder.repository;

import com.ogul.wordfinder.dto.WordResponse;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

public interface CustomWordRepository {

    List<WordResponse> findMatchingWords(
        Map<Character, Integer> letterCounts, Integer minLength, Integer maxLength, Pageable pageable
    );
}
