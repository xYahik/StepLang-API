package com.example.steplang.mappers;

import com.example.steplang.dtos.language.LanguageGetDTO;
import com.example.steplang.dtos.language.WordGetDTO;
import com.example.steplang.entities.language.Language;
import com.example.steplang.entities.language.Word;
import com.example.steplang.entities.language.WordCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = WordMapper.class)
public interface LanguageMapper {
    @Mapping(source ="words", target = "words")
    LanguageGetDTO toDto(Language language);
}
