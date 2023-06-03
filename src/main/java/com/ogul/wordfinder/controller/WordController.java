package com.ogul.wordfinder.controller;

import com.ogul.wordfinder.dto.WordResponse;
import com.ogul.wordfinder.service.WordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WordController {

    private final WordService wordService;

    @GetMapping("/{input}")
    public List<WordResponse> getMatchingWords(
        @PathVariable(name = "input") String input,
        @RequestParam(name = "min", required = false) Integer minLength,
        @RequestParam(name = "max", required = false) Integer maxLength,
        @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable
    ) {
        return wordService.getMatchingWords(input, minLength, maxLength, pageable);
    }

    @PostMapping("/load")
    public ResponseEntity<String> loadWords() {
        return wordService.loadWords();
    }
}
