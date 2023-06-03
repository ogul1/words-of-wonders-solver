package com.ogul.wordfinder.repository;

import com.ogul.wordfinder.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {

}
