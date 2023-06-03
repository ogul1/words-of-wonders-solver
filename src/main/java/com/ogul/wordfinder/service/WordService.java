package com.ogul.wordfinder.service;

import com.ogul.wordfinder.dto.WordResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface WordService {

    List<WordResponse> getMatchingWords(String input, Integer minLength, Integer maxLength, Pageable pageable);

    ResponseEntity<String> loadWords();
}
