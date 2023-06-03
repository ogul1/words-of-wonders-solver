package com.ogul.wordfinder.service.impl;

import com.ogul.wordfinder.dto.WordResponse;
import com.ogul.wordfinder.entity.Word;
import com.ogul.wordfinder.repository.CustomWordRepository;
import com.ogul.wordfinder.repository.WordRepository;
import com.ogul.wordfinder.service.WordService;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;
    private final CustomWordRepository customWordRepository;

    @Value("${words.path}")
    private String wordsPath;

    @Override
    public List<WordResponse> getMatchingWords(String input, Integer minLength, Integer maxLength, Pageable pageable) {

        if (minLength == null) {
            minLength = 3;
        }
        if (maxLength == null) {
            maxLength = input.length();
        }

        Map<Character, Integer> letterCounts = new HashMap<>();

        for (char c: input.toCharArray()) {
            letterCounts.put(c, letterCounts.getOrDefault(c, 0) + 1);
        }

        return customWordRepository.findMatchingWords(letterCounts, minLength, maxLength, pageable);
    }

    @Override
    @Transactional
    public ResponseEntity<String> loadWords() {
        if (wordRepository.count() != 0) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Words already loaded.");
        }
        try {
            Path path = Paths.get(wordsPath).toAbsolutePath().normalize();
            BufferedReader reader = new BufferedReader(new FileReader(path.toString()));

            String line;
            while ((line = reader.readLine()) != null) {
                Word word = Word.builder()
                    .word(line)
                    .build();
                wordRepository.save(word);
            }
            return ResponseEntity.ok().body("Words loaded.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
