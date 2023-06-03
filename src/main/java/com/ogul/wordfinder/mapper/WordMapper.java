package com.ogul.wordfinder.mapper;

import com.ogul.wordfinder.dto.WordResponse;
import com.ogul.wordfinder.entity.Word;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WordMapper {

    WordResponse map(Word word);
}
